(ns ad2022.04
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data
  (->> (io/reader (io/resource "04"))
       (line-seq)
       (map (fn [l] (string/split l #"[,\-]")))
       (map (partial map read-string))))

(defn fully-contains?
  [[fl fr sl sr]]
  (<= (+ (max fl sl)
         (min (- fr fl) (- sr sl)))
      (min fr sr)))

(defn overlaps?
  [[fl fr sl sr]]
  (nat-int? (- (min fr sr)
               (max fl sl))))

(defn part-one
  [data]
  (count (filter fully-contains? data)))

(defn part-two
  [data]
  (count (filter overlaps? data)))

(part-one data)                         ; 477
(part-two data)                         ; 830
