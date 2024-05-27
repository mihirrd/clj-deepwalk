(ns clj-deepwalk.core
  (:require [loom.graph :as lg]
            [loom.io :as lio]
            [clojure.java.io :as io]))


(defn random-walk
  "Generates a random walk in the graph starting from `start-node` of length
  `walk-length`."
  [graph start-node walk-length]
  (loop [current-node start-node
         path [start-node]
         remaining-steps (dec walk-length)]
    (if (zero? remaining-steps)
      path
      (let [neighbors (lg/successors graph current-node)]
        (if (empty? neighbors)
          path ;; End the walk if no neighbors are available
          (let [next-node (rand-nth (vec neighbors))]
            (recur next-node (conj path next-node) (dec remaining-steps))))))))


(defn generate-random-walks
  "Given a `graph`, `num-walks` (number of walks) to be generated per node and `walk-length`,
  returns an array of random walks from each node in the graph"
  [graph num-walks walk-length]
  (let [nodes (lg/nodes graph)]
    (mapcat (fn [node]
              (repeatedly num-walks #(random-walk graph node walk-length)))
            nodes)))


(defn generate-pairs
  "Generates context-target pairs from random walks."
  [walks window-size]
  (mapcat
    (fn [walk]
      (for [i (range (count walk))
            j (range (max 0 (- i window-size))
                     (min (count walk) (+ i window-size 1)))
            :when (not= i j)]
        [(nth walk i) (nth walk j)]))
    walks))
