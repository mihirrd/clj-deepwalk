(ns model.core
  (:require [graphs.core :as gc]))


(defn sigmoid
  [x]
  (/ 1 (+ 1 (Math/exp (- x)))))


(defn train-pair
  "Train on a single word pair"
  [word context learning-rate vocab vectors]
  (let [word-idx (get vocab word)
        context-idx (get vocab context)
        word-vec (get vectors word-idx)
        context-vec (get vectors context-idx)
        dot-product (reduce + (map * word-vec context-vec))
        error (- 1 (sigmoid dot-product))
        word-delta (mapv #(* learning-rate error %) context-vec)
        context-delta (mapv #(* learning-rate error %) word-vec)]
    (-> vectors
        (update word-idx #(mapv + % word-delta))
        (update context-idx #(mapv + % context-delta)))))


(defn get-context-words
  [idx walk window-size]
  (->> (range (max 0
                   (- idx window-size))
              (min (count walk)
                   (+ idx window-size 1)))
       (remove #(= % idx))
       (map #(nth walk %))))


(defn process-walk
  "trains the skip gram model for one random walk"
  [{:keys [walk embedding vocab window-size learning-rate]}]
  (reduce
   (fn [embedding [idx word]]
     (let [context-words (get-context-words idx walk window-size)]
       (reduce
        #(train-pair word %2 learning-rate vocab %1)
        embedding
        context-words)))
   embedding
   (map-indexed vector walk)))


(defn init-embedding
  "Creates random embedding for size m x n where m = vector-size, n = # of nodes"
  [vector-size vocab-size]
  (->> rand
       ((partial repeatedly vector-size))
       (partial vec)
       ((partial repeatedly vocab-size))
       vec))


(defn train-epochs
  [graph {:keys [epochs embedding nodes vocab window-size
                 learning-rate walk-length]}]
  (reduce
   (fn [embedding _]
     (reduce
      (fn [embedding start-node]
        (let [walk (gc/random-walk graph start-node walk-length)]
          (process-walk {:embedding embedding
                         :walk walk
                         :vocab vocab
                         :window-size window-size
                         :learning-rate learning-rate})))
      embedding
      nodes))
   embedding
   epochs))


;; Training process
(defn train-deepwalk
  "Train DeepWalk model"
  [graph & {:keys [vector-size walk-length num-walks window-size learning-rate]
            :or {vector-size 5
                 walk-length 10
                 num-walks 10
                 window-size 5
                 learning-rate 0.025}}]
  (let [vocab (gc/create-vocab graph)
        vocab-size (count vocab)
        embedding (init-embedding vector-size vocab-size)
        nodes (keys graph)
        epochs (range num-walks)]
    (train-epochs graph
                  {:epochs epochs
                   :embedding embedding
                   :nodes nodes
                   :vocab vocab
                   :window-size window-size
                   :learning-rate learning-rate
                   :walk-length walk-length})))
