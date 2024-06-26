(defproject clj-deepwalk "0.1.0-SNAPSHOT"
  :description "Clojure library to provide core functionalities for graph embedding"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aysylu/loom "1.0.2"]
                 [org.deeplearning4j/deeplearning4j-nlp "1.0.0-beta7"]
                 [org.deeplearning4j/deeplearning4j-core "1.0.0-beta7"]
                 [org.nd4j/nd4j-native-platform "1.0.0-beta7"]]
  :main ^:skip-aot clj-deepwalk.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
