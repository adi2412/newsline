# newsline

This is an application in clojure which would accept a news article as input and categorise it into any of the given categories.

## Installation


## Usage

		If running through lein use,

		lein run <filename.txt>

    $ java -jar newsline-0.1.0-standalone.jar [args]


## Issues

Words like "going" do not get recognised by wordnet at all for some reason. Need to figure a way out for this.

So Stemming was basically the shitiest idea in the world because half the time it doesn't work correctly.

It seems defining wordnet words without a pos tag actually is better because more words get recognised then.

Ditching the stemming idea initially(since the related synsets would automatically give us stemmed words) and now have to figure how to get more words to be recognised by wordnet.

## Next Steps

	Make a list of all the synset words gathered and find the synset for each.
	Need to figure out how to add activation functions for this.

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
