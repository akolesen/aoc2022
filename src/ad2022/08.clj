(ns ad2022.08
  (:require [clojure.java.io :as io]))

(def data (->> (io/reader (io/resource "08"))
               (line-seq)
               (map (partial map (fn [x] (- (int x) 48))))))

(defn transpose
  [coll]
  (apply mapv vector coll))

(defn row-visible
  [acc [coords item]]
  (if (or (empty? acc)
          (< (second (peek acc)) item))
    (conj acc [coords item])
    acc))

(defn part-one
  [coll]
  (->> (concat coll
               (map reverse coll)
               (transpose coll)
               (map reverse (transpose coll)))
       (mapcat (partial reduce row-visible []))
       (set)
       (count)))

(defn index
  [coll]
  (for [[y row] (map-indexed vector coll)]
    (for [[x item] (map-indexed vector row)]
      [[x y] item])))

(part-one (index data))                         ;1818

(defn visible-from
  [from coll]
  (reduce (fn [acc item]
            (if (<= from item) (reduced (inc acc))
                (inc acc))) 0 coll))

(defn measure
  [row]
  (->> row
       (map-indexed vector)
       (map (fn [[x _]] (let [[head [from & tail]] (split-at x row)]
                          (* (visible-from from (reverse head))
                             (visible-from from tail)))))))

(defn part-two
  [data]
  (->> (mapcat (partial map *)
               (map measure data)
               (->> data transpose (map measure) transpose))
       (apply max)))

(part-two data)                       ; 368368
