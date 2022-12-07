(ns ad2022.07
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data
  (->> (io/reader (io/resource "07"))
       (line-seq)))

(defn store
  [state paths size]
  (reduce (fn [m path] (update m (apply str path) (fnil + 0) size)) state paths))

(defn process
  [xs]
  (loop [paths []
         xs    xs
         state {}]
    (if-let [cmd (first xs)]
      (let [cmd-seq (string/split cmd #" ")
            xs      (rest xs)]
        (cond (= "dir"     (cmd-seq 0)) (recur paths xs state)
              (= "$ ls"    cmd)         (recur paths xs state)
              (= "$ cd .." cmd)         (recur (pop paths) xs state)
              (= "$ cd" (subs cmd 0 4)) (recur (conj paths (into (peek paths) (cmd-seq 2))) xs state)
              :else                     (recur paths xs (store state paths (read-string (cmd-seq 0))))))
      state)))

(def fs (process data))
(def total (fs "/"))

(->> fs (vals) (remove (partial < 100000)) (reduce +)) ;1206825
(->> fs (vals) (filter (partial < (- total 40000000))) (sort) (first)) ;9608311
