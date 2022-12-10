(ns ad2022.10
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data (->> (io/reader (io/resource "10"))
               (line-seq)
               (map (fn [s]
                      (let [[d n] (string/split s #" ")] [d (when n (read-string n))])))
               (reduce (fn [acc [op val]]
                         (let [last (peek acc)]
                           (cond (= op "addx") (into acc [last (+ val last)])
                                 (= op "noop") (into acc [last])))) [1])))

(defn part-one
  [data]
  (->> data
       (drop 19)
       (take-nth 40)
       (zipmap (range 20 221 40))
       (map (partial reduce *))
       (reduce +)))

(part-one data)                         ; 13760

(defn part-two
  [data]
  (->> data
       (map-indexed vector)
       (map (fn [[cycle x]] (if (< (abs (- (rem cycle 40) x)) 2) "#" ".")))
       (partition 40)
       (map println)))

(part-two data) ; RFKZCPEF

;; (# # # . . # # # # . # . . # . # # # # . . # # . . # # # . . # # # # . # # # # .)
;; (# . . # . # . . . . # . # . . . . . # . # . . # . # . . # . # . . . . # . . . .)
;; (# . . # . # # # . . # # . . . . . # . . # . . . . # . . # . # # # . . # # # . .)
;; (# # # . . # . . . . # . # . . . # . . . # . . . . # # # . . # . . . . # . . . .)
;; (# . # . . # . . . . # . # . . # . . . . # . . # . # . . . . # . . . . # . . . .)
;; (# . . # . # . . . . # . . # . # # # # . . # # . . # . . . . # # # # . # . . . .)
