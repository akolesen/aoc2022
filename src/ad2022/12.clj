(ns ad2022.12
  (:require [clojure.java.io :as io]))

(def unvisited-w (* 1000 1000))

(defn prepare-graph
  [data]
  (into
    {}
    (apply
      concat
      (for [[y-idx line] (map-indexed vector data)]
        (for [[x-idx char] (map-indexed vector line)] [[x-idx y-idx] {:char char :visited false :w unvisited-w}])))))

(def data
  (->> (io/resource "12")
       (io/reader)
       (line-seq)
       (prepare-graph)))

(defn find-char
  [graph c]
  (->> graph
       (filter (comp #{c} :char val))
       (first)
       (key)))

(defn unvisited-neighbours
  [graph [x y]]
  (->> (select-keys graph [[(+ x 1) y]
                           [(- x 1) y]
                           [x (+ y 1)]
                           [x (- y 1)]])
       (remove (fn [[_ {:keys [visited]}]] visited))))

(defn possible-turns
  [graph [current-coords {:keys [char]}]]
  (->> (unvisited-neighbours graph current-coords)
       (filter (fn [[_ {next-char :char}]] (or (and (= char \z) (= next-char \E))
                                               (and (= char \S) (#{\a \b \c} next-char))
                                               (and (<= (- (int next-char) (int char)) 1)
                                                    (not= next-char \E)))))
       (into {})))

(defn update-w
  [[_ current] graph [next-coords {:keys [w]}]]
  (let [new-w (+ (current :w) 1)]
    (if (> w new-w)
      (assoc-in graph [next-coords :w] new-w)
      graph)))

(defn search
  [graph begin-key]
  (loop [graph     (update graph begin-key (fn [v] (assoc v :w 0 :char \S)))
         unvisited (select-keys graph [begin-key])]
    (if (empty? unvisited)
      (get graph (find-char graph \E))
      (let [[current-key _ :as current] (->> (keys unvisited)
                                             (select-keys graph)
                                             (apply (partial min-key (comp :w val))))
            to-visit                    (possible-turns graph current)
            unvisited                   (-> unvisited
                                            (merge to-visit)
                                            (dissoc current-key))
            graph                       (-> (reduce (partial update-w current) graph to-visit)
                                            (assoc-in [current-key :visited] true))]

            (recur graph unvisited)))))

(:w (search data (find-char data \S)))                           ; 520

(defn part-two
  [data]
  (->> (assoc-in data [(find-char data \S) :char] \a)
       (filter (comp #{\b} :char val))
       (map first)
       (map (partial search data))
       (map :w)
       (apply min)))

(+ (part-two data) 1)                   ; 508
