Test on
2.4 2.9.1 and
2.5RC2 2.10.0



Hi!!


When I test my application, I discover that by default, filter based on sitemap is only apply on html file and not on any other file type like *.jpg *.js *.toto ...

If it's normal, how could we apply same permissions filter for all resources under webapp?
if it's not normal you can test it with last version git://github.com/lift/lift_25_sbt.git, and put a file like "toto.titi" at webapp root for example and request it.
