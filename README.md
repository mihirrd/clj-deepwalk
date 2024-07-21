# DeepWalk Clojure Library

## Overview

This library implements the DeepWalk algorithm for learning continuous feature
representations for nodes in networks. DeepWalk uses short random walks to learn
representations of vertices in a graph, which can be used for various downstream
machine learning tasks such as node classification, link prediction, and
community detection.

## Usage

```clojure
(require '[deepwalk.core :as dw])

(def edges [[0 1] [1 2] [2 3] [3 0] [0 2] [1 3]])
(def result (dw/deepwalk edges))
```

### Using custom opts
```clojure
(def result (dw/deepwalk edges
                         :vector-size 128
                         :walk-length 20
                         :num-walks 100
                         :window-size 10
                         :learning-rate 0.025))
```

## API Reference

edges: A sequence of edges representing the graph. Each edge is a vector of two node identifiers.
opts: Optional parameters (key-value pairs)

- `:vector-size` (default: 64): Dimensionality of the feature representations
- `:walk-length` (default: 10): Length of each random walk
- `:num-walks` (default: 10): Number of random walks per node
- `:window-size` (default: 5): Maximum distance between the current and predicted node in the skip-gram model
- `:learning-rate` (default: 0.025): Learning rate for the skip-gram model

Returns a map containing:

- `:graph` The input graph represented as an adjacency list
- `:vectors` The learned feature representations for each node


## Limitations and Future Work

This implementation is a basic version of DeepWalk and has some limitations:

- It may not be efficient for very large graphs.
- There currently isn't any parallelization for random walk generation or training.
