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


(defn create-vocab
  "Indexes graph nodes. Creates a map of keys being nodes and the corresponding
  values are incrementing integers. As each graph node is analogous to a word in
  NLP terms, this function essentially creates vocab - collection of
  words (indexed)"
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
