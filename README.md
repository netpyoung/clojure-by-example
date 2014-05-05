https://help.github.com/articles/creating-project-pages-manually

https://github.com/gdeer81/marginalia
[marginalia.parser :as parser]
            [marginalia.html :as html]
            [marginalia.core :as cc]




(->> (slurp "filename")
     (parser/parse)
     (map html/section-to-html))

(spit "filename" "output-contents")


(->> (cc/path-to-doc "filename")
     (:groups)
     (map html/section-to-html))


mustach
https://github.com/davidsantiago/stencil


markdwon

https://github.com/yogthos/markdown-clj
table 지원안함.


http://alexgorbatchev.com/SyntaxHighlighter/

https://github.com/Raynes/fs
