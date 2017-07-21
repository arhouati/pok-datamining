## Synopsis
This library is a datamining process. it take as input a text and gives a score (int) which describe the orientation of opinion of the given text.

## Code Example

Check Main class to have a complete example code :

			score = DataMining.process( text , lang);

The score is an int, and can have trees values :
	0 for neutral opinion
	> 1 for positive opinion
	and < 1 for negative opinion

## Motivation

"pok-datamining" is a part of a platform named "POK". The main goal of this platform is retrieve public opinion from text content of blogs and social media networks.

## API Reference

This result is a part of research work, here is some of our publication :
https://link.springer.com/chapter/10.1007%2F978-3-319-30298-0_43 
http://ieeexplore.ieee.org/document/7847689/
https://link.springer.com/chapter/10.1007/978-3-319-46568-5_35

## Tests

TODO : Implement TU

## Contributors

use GIT to track issues

## RoadMap

1/ Implement DeapLearning and Neural Network
2/ implemented Arabic language

## Release Note

V1.0 (current) : standard algorithm using openNLP and Dictionary, handle only french text

## License

This api is published on MTI license, please read "License" file for more information. 


## references
Here is a list of api as libraries used by pok-datamining :
1/ Dictionary : Feel (http://advanse.lirmm.fr/feel.php)
2/ NLP : Apache openNLP (https://opennlp.apache.org/)
