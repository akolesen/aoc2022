(ns ad2022.05
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data
  (->> (io/reader (io/resource "05"))
       (line-seq)
       (split-with seq)))

(defn transpose
  [& coll]
  (apply map list coll))

(defn prepare-schema
  [schema]
  (->> schema
       (map (partial partition-all 4))
       (map (partial map #(nth % 1)))
       (reverse)
       (apply transpose)
       (mapv (partial remove (partial = \space)))))

(defn parse-move
  [line]
  (when line
    (let [[_ n _ move-from _ move-to] (string/split line #" ")]
      (map read-string [n move-from move-to]))))

(defn handle
  [[schema moves] crane-fn]
  (loop [schema              (prepare-schema schema)
         [move & moves-left] (rest moves)]
    (if-let [[n moving-from moving-to] (parse-move move)]
      (recur (-> schema
                 (update (- moving-from 1) (partial drop-last n))
                 (update (- moving-to 1) concat (crane-fn n (schema (- moving-from 1)))))
             moves-left)
      (->> schema
           (map last)
           (apply str)))))

(handle data (comp reverse take-last))   ; DHBJQJCCW
(handle data take-last)                  ; WJVRLSJJT
