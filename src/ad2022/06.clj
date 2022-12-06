(ns ad2022.06
  (:require [clojure.java.io :as io]))

(def data
  (->> (io/reader (io/resource "06"))
       (slurp)))

(defn scan
  [stream l]
  (loop [acc    (reverse (take l stream))
         stream (drop l stream)]
    (if (apply distinct? (take l acc))
      (count acc)
      (recur (conj acc (first stream))
             (rest stream)))))

(scan data 4)                             ; 1544
(scan data 14)                            ; 2145
