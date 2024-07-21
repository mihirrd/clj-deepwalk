(ns graphs.core)

;; Graph representation
(defn create-graph
  "Create a graph from a sequence of edges"
  [edges]
  (reduce (fn [graph [u v]]
            (-> graph
                (update u (fnil conj #{}) v)
                (update v (fnil conj #{}) u)))
          {}
          edges))


(defn get-neighbors
  "Get neighbors of a node in the graph"
  [graph node]
  (get graph node))


(defn index-graph-nodes
  "Indexes graph nodes. Creates a map of incremental vals being keys and the
  corresponding values are the graph nodes"
  [graph]
  (zipmap (keys graph) (range)))


;; Random walk generation
(defn random-walk
  "Generate a random walk of length 'walk-length' starting from 'start-node'"
  [graph start-node walk-length]
  (loop [walk [start-node]
         current-node start-node
         steps-left (dec walk-length)]
    (if (zero? steps-left)
      walk
      (let [neighbors (get-neighbors graph current-node)
            next-node (rand-nth (seq neighbors))]
        (recur (conj walk next-node)
               next-node
               (dec steps-left))))))
