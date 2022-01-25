(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'io.sixtant/dydx-starkware)
(def version "0.1.2")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn build [_]
  (b/javac {:src-dirs ["src/java"]
            :class-dir class-dir
            :basis basis
            :javac-opts ["-source" "8" "-target" "8"]})
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src/io"]
                  :class-dir class-dir}))

(defn jar [_]
  (clean nil)
  (build nil)
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn uber [_]
  (clean nil)
  (build nil)
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :exclude [".*bouncycastle.*"]}))
