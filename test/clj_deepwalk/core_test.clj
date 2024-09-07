(ns clj-deepwalk.core-test
  (:require [clojure.test :refer :all]
            [graphs.core :as gc]))


(deftest test-create-graph
  (let [edges-1 [[:a :b] [:a :c] [:b :d]]
        edges-2 [[:a :b] [:c :d]]]
    (testing "create-graph"
      (is (= {:a #{:b :c}
              :b #{:a :d}
              :c #{:a}
              :d #{:b}}
             (gc/create-graph edges-1)))
      (is (= {:a #{:b}
              :b #{:a}
              :c #{:d}
              :d #{:c}}
             (gc/create-graph edges-2))))))
