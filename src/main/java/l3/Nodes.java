package l3;



//
// Node -> Read -> ReadName \
//              -> Write    / WriteName
//      -> Apply

public interface Nodes {
    interface Node {
        interface App extends Node { }
        interface Lam extends Node { }
        interface Var extends Node { }
    }

    interface Read { // as a namespace
        interface App extends Node.App {
            Node getLeft();
            Node getRight();
        }
        interface Lam extends Node.Lam {
            Node getBody();
        }
        interface Var extends Node.Var {
        }
    }

    interface ReadName {
        interface App extends Read.App {
        }
        interface Lam extends Read.Lam {
            String getParam();
        }
        interface Var extends Read.Var {
            String getName();
        }
    }

    interface Write {
        interface App extends Read.App {
            void setLeft(Node node);
            void setRight(Node node);
        }
        interface Lam extends Read.Lam {
            void setBody(Node node);
        }
        interface Var extends Read.Var {
        }
    }

    interface WriteName {
        interface App extends ReadName.App, Write.App {
        }
        interface Lam extends ReadName.Lam, Write.Lam {
            void setParam(String param);
        }
        interface Var extends ReadName.Var, Write.Var {
            void setName(String name);
        }
    }

    interface Apply {
        interface App extends Node.App {
            Node apply();
        }
        interface Lam extends Node.Lam {
            Node apply(Node arg);
        }
        interface Var extends Node.Var {
        }
    }

    interface Factory {
        Read.App newApp(Node left, Node right);
        Read.Lam newLam(String param, Node body);
        Read.Var newVar(String name);
    }
}
