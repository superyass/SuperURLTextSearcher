# SuperURLTextSearcher
### CLI program for searching URLs written in Java

urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";

defaultExtensions = {"txt", "md", "java", "php", "py", "c", "pl", "rb", "cs","json","smali"};

#### Parametres :

{"-input", "-in"}, description = "Input folder :"
{"-search", "-s"}, description = "comma separated search keywords"
{"-ExcludedSearch", "-xs"}, description = "comma separated keywords to be excluded"
{"-addExtensions", "-addExt", "-ae"}, description = "comma separated extensions to be added"
{"-excludExtensions", "-execExt", "-exce"}, description = "comma separated extensions to be excluded"
{"-exclExtensions", "-exclExt", "-exle"}, description = "comma separated extensions to be exclusively looked for"
"-debug", description = "Debug mode"
{"-h", "-help", "--help"}, description = "help mode"
