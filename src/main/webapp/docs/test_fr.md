# In French Aide-mémoire de la syntaxe Markdown

EMPHASE

*italic*   **gras**
_italic_   __gras__
LIEN

Incorporé :
Un [exemple](http://url.com/ "Titre")
Par référence (le titre est facultatif) :
Un [exemple][id]. Ensuite, n'importe où
dans le document, définissez le lien :

  [id]: http://exemple.com/  "Titre"
IMAGES

Incorporé (le titre est facultatif) :
![texte alternatif](/img.jpg "Titre")
Par référence :
![texte alternatif][id]

  [id]: /url/vers/img.jpg "Titre"
TITRE

Style Setext :
Titre 1
=======

Titre 2
-------
Style atx (dièses fermants optionnels) :
# Titre 1 #

## Titre 2 ##

###### Titre 6
LISTE

Ordonnée, sans paragraphe :
1.  Foo
2.  Bar
Non-ordonnée, avec paragraphes :
*   Un élément de liste.

    Avec un deuxième paragraphe.

*   Bar
Listes imbriquées :
*   Abacus
    * ass
*   Bastard
    1.  bitch
    2.  bupkis
        * BELITTLER
    3. burper
*   Cunning
BLOC DE CITATION

> Les chevronts sont utilisés pour
> les citation comme dans le courriel.

> > Et il peuvent être imbriqués.

> #### Titre dans la citation
>
> * Vous pouvez citer une liste.
> * Etc.
ÉTENDUE DE CODE

Une étendue de `<code>` est délimitée
par des guillemets obliques.

Vous pouvez inclure des guillemets
obliques en tant que caractère
littéraux comme `` `ceci` ``.
BLOC DE CODE

Indentez toutes les lignes d'un bloc de code par 4 espaces ou une tabulation.
Paragraphe normal.

    Ceci est un
    bloc de code.
SÉPARATEUR HORIZONTAL

Au moins trois tirets ou astériques :
---

* * *

- - - -
SAUT DE LIGNE FORCÉ

Terminez la ligne par deux espaces ou plus :
Les roses sont rouges,
Les violettes sont bleus.

## About

I blog here.  This is a page that tells people about me.

## Contact Me
**foo**
You can put some contact information here

[title: About]: /
[order: 20]: /

> A Blockquote
> is started with ">"

* unordered Item A
* unordered Item B

1. ordered Item A
2. ordered Item B

* items separated by empty lines

* are turned into

* separate paragraphs

1.  you can continue
on the next line

2.  or you can continue by
indenting four spaces or a tab

**strong**  __also strong__
*emphasis*  _also emphasis_
***strong and italic***
[some text](http://example.com/ "Title")
[link without title](http://example.com/)
use url as text: <http://example.com>

> Blockqoutes
> > can nest blockquotes
> >     // or code
> * or
> * lists
> > ##nested blockquotes##
> > > can again contain
> > > 1. nested
> > > 2. content

## Lift embed in Markdown
<span class="lift:helloWorld.howdy">Welcome to your Lift app at <span id="time">Time goes here</span>
</span>