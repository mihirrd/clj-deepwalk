# DeepWalk Clojure Library

## Overview

This library implements the DeepWalk algorithm for learning continuous feature
representations for nodes in networks. DeepWalk uses short random walks to learn
representations of vertices in a graph, which can be used for various downstream
machine learning tasks such as node classification, link prediction, and
community detection.

More on this here - [Deepwalking in clojure](https://medium.com/@deshpande.mihir7/deepwalking-in-clojure-0b2c4eacd4e0)

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

### Sample output
```
=> (def edges [[:a :b] [:b :c] [:b :d]])

=> (dw/deepwalk edges :vector-size 5)

=> {:graph {:a #{:b}, :b #{:c :d :a}, :c #{:b}, :d #{:b}},
   :vectors
   [[1.2962443195668192
     0.6028800604250182
     0.651273176448106
     1.2017765618740872
     1.435104748831545]
    [1.5642613286885012
     0.7275341153860508
     0.7859331986660882
     1.45026101413665
     1.731833133096857]
    [1.280047368643186
     0.5953468981930664
     0.6431353281138268
     1.1867600131417053
     1.4171726963347773]
    [1.2820077614684258
     0.5962586720979554
     0.6441202900095928
     1.1885775363615692
     1.4193430966293958]]}
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
