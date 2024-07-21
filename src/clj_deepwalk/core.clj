(ns clj-deepwalk.core
  (:require [graphs.core :as gc]
            [model.core :as mc]))


(defn deepwalk
  "Run DeepWalk algorithm on the given graph"
  {:opts [:vector-size
          :walk-length
          :num-walks
          :window-size
          :learning-rate]}
  [edges & opts]
  (let [graph (gc/create-graph edges)
        trained-vectors (mc/train-deepwalk graph (apply hash-map opts))]
    {:graph graph
     :vectors trained-vectors}))
