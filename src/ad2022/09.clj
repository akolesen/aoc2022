(ns ad2022.09
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def coord {"L" [dec identity] "R" [inc identity] "U" [identity inc] "D" [identity dec]})
(def data (->> (io/reader (io/resource "09"))
               (line-seq)
               (map (fn [s]
                      (let [[d n] (string/split s #" ")
                            n     (read-string n)] [d n])))))

(defn step-fn
  [diff]
  (cond (< diff 0) dec
        (= diff 0) identity
        (> diff 0) inc))

(defn step-tail
  [[x-head y-head] [x-tail y-tail]]
  (let [x-diff (- x-head x-tail)
        y-diff (- y-head y-tail)]
    (if (and (<= (abs y-diff) 1)
             (<= (abs x-diff) 1))
      [x-tail y-tail]
      [((step-fn x-diff) x-tail) ((step-fn y-diff) y-tail)])))

(defn step-head
  [head direction]
  (let [[x-head y-head] (peek head)
        [x-fn y-fn]     (get coord direction)
        new-head        [(x-fn x-head) (y-fn y-head)]]
    (conj head new-head)))

(defn step-rest
  [state item-state]
  (let [head (peek (peek state))
        tail (peek item-state)]
    (conj state (conj item-state (step-tail head tail)))))

(defn step
  [[head-state & rest-state] direction]
  (reduce step-rest [(step-head head-state direction)] rest-state))

(defn walk
  [state [direction steps]]
  (reduce (fn [state _] (step state direction)) state (range steps)))

(count (set (last (reduce walk (list [[0 0]] [[0 0]]) data)))) ; 6269
(count (set (last (reduce walk (list [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]]) data)))) ; 2557
