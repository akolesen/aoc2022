(ns ad2022.13
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse
  [s]
  (->> (string/split s #"\n")
       (map read-string)
       (map eval)))

(def data (map parse (string/split (slurp (io/resource "13"))#"\n\n")))

(defn compare-signals
  [left right]
  (let [left-item  (first left)
        right-item (first right)]
    (cond (and (empty? left) (empty? right)) nil
          (empty? left)                      true
          (empty? right)                     false
          (or (seqable? left-item)
              (seqable? right-item))         (let [left-item  (if-not (seqable? left-item) [left-item] left-item)
                                                   right-item (if-not (seqable? right-item) [right-item] right-item)]
                                               (if-some [res (compare-signals left-item right-item)]
                                                 res
                                                 (compare-signals (rest left) (rest right))))
          (< left-item right-item)           true
          (> left-item right-item)           false
          :else                              (compare-signals (rest left) (rest right)))))

(->> data
     (map (partial apply compare-signals))
     (map-indexed vector)
     (filter (comp true? second))
     (map (comp inc first))
     (reduce +))                                  ; 5675

(def markers #{[[2]] [[6]]})

(->> data
     (apply concat)
     (concat markers)
     (sort compare-signals)
     (map-indexed vector)
     (filter (comp markers second))
     (map (comp inc first))
     (reduce *))                        ; 20383
