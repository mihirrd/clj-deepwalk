(ns model.core
  (:require [graphs.core :as gc]))

(defn sigmoid [x]
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

;; Training process
(defn train-deepwalk
  "Train DeepWalk model"
  [graph {:keys [vector-size walk-length num-walks window-size learning-rate]
          :or {vector-size 64
               walk-length 10
               num-walks 10
               window-size 5
               learning-rate 0.025}}]
  (let [vocab (gc/create-vocab graph)
        vocab-size (count vocab)
        vectors (vec (repeatedly vocab-size #(vec (repeatedly vector-size rand))))
        nodes (keys graph)]
    (reduce
     (fn [vecs _]
       (reduce
        (fn [vecs start-node]
          (let [walk (gc/random-walk graph start-node walk-length)]
            (reduce
             (fn [vecs [i word]]
               (let [context-words (->> (range (max 0 (- i window-size))
                                               (min (count walk) (+ i window-size 1)))
                                        (remove #(= % i))
                                        (map #(nth walk %)))]
                 (reduce
                  #(train-pair word %2 learning-rate vocab %1)
                  vecs
                  context-words)))
             vecs
             (map-indexed vector walk))))
        vecs
        nodes))
     vectors
     (range num-walks))))
