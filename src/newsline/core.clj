(ns newsline.core
  (:gen-class)
  (:use [clj-wordnet.core]
  					[opennlp.nlp]
  					[opennlp.tools.filters]
  					[opennlp.treebank]))

(def get-sentences (make-sentence-detector "models/en-sent.bin"))
(def tokenize (make-tokenizer "models/en-token.bin"))
(def pos-tag (make-pos-tagger "models/en-pos-maxent.bin"))
(def name-find (make-name-finder "models/en-ner-person.bin"))
(def chunker (make-treebank-chunker "models/en-chunker.bin"))

(def wordnet (make-dictionary "models/dict/"))



(defn lemma
	[word]
	(:lemma word))

(defn word-net
	"Returns a word in the IWord format of JWI"
	[word tag]
	(first (wordnet word tag)))	

(defn relatedSynsets
	"Returns the related synsets of the given word"
	[word]
	(if (not (= word nil)) (flatten (vals (related-synsets word :similar-to)))))

(defn postagger
	"Returns whether the word is a noun or a verb"
	[tag]
	(if (or (= (.toString tag) "NN") (= (.toString tag) "NNS") (= (.toString tag) "NNP") (= (.toString tag) "NNPS"))
					"noun" "verb"))

(defn -main
	"Main driver function."
  [& args]
      (def s (slurp (apply str (first args))))
      (def sentences (get-sentences s))
  		(def tokens (nouns-and-verbs (pos-tag (tokenize s))))
   		(def counts (count tokens))
  		(loop [i 0]
  			(when (< i counts)
  				(def word ((first tokens) 0))
  				(def postag (postagger ((first tokens) 1)))
  				(println (first tokens))
  				(println postag)
  				(def wordnetWord (word-net word postag))
  				(if (not (= wordnetWord nil)) (println (:lemma wordnetWord)))
  				(def synsetWords (relatedSynsets wordnetWord))
  				(if (not (= synsetWords nil)) (println (map :lemma synsetWords)))
  				(def tokens (rest tokens))
  				(recur (inc i)))))

