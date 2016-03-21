package l3;



//
// Node -> Read -> ReadName \
//              -> Write    / WriteName
//              -> Apply

public interface Nodes {
    public interface Node {
    }

    public interface Read { // as a namespace
        public interface App extends Node {
            Node getLeft();
            Node getRight();
        }
        public interface Lam extends Node {
            Node getBody();
        }
        public interface Var extends Node {
        }
    }

    public interface ReadName {
        public interface App extends Read.App {
        }
        public interface Lam extends Read.Lam {
            String getParam();
        }
        public interface Var extends Read.Var {
            String getName();
        }
    }

    public interface Write {
        public interface App extends Read.App {
            void setLeft(Node node);
            void setRight(Node node);
        }
        public interface Lam extends Read.Lam {
            void setBody(Node node);
        }
        public interface Var extends Read.Var {
        }
    }

    public interface WriteName {
        public interface App extends ReadName.App, Write.App {
        }
        public interface Lam extends ReadName.Lam, Write.Lam {
            void setParam(String param);
        }
        public interface Var extends ReadName.Var, Write.Var {
            void setName(String name);
        }
    }
    public interface Apply {
        public interface App extends Read.App {
            public default Node apply() { return ((Lam) getLeft()).apply(getRight()); }
        }
        public interface Lam extends Read.Lam {
            @Override
            public default Node getBody() { throw new UnsupportedOperationException("ouch"); }
            public Node apply(Node arg);
        }
        public interface Var extends Read.Var {
        }
    }

    public interface Factory {
        public Read.App newApp(Node left, Node right);
        public Read.Lam newLam(String param, Node body);
        public Read.Var newVar(String name);
    }
}
