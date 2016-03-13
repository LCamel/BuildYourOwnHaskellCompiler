package l3;




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
    public interface ReadWithName {
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
    public interface WriteWithName {
        public interface App extends ReadWithName.App, Write.App {
        }
        public interface Lam extends ReadWithName.Lam, Write.Lam {
            void setParam(String param);
        }
        public interface Var extends ReadWithName.Var, Write.Var {
            void setName(String name);
        }
    }

    public interface Factory {
        public Read.App newApp(Node left, Node right);
        public Read.Lam newLam(String param, Node body);
        public Read.Var newVar(String name);
    }
}
