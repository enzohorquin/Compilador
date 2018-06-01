import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

public class Tree {

    final String name;
    final List<Tree> children;

    public Tree(String name, List<Tree> children) {
        this.name = name;
        this.children = children;
    }

    public void print(String title) {
    	File file = new File(title+"_arbol.txt");
    	try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PrintWriter pw;
		try {
			pw = new PrintWriter(file);
			print("", true, pw);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void print(String prefix, boolean isTail, PrintWriter pw) {
        pw.println(prefix + ("|-> ") + name);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "|   "), false, pw);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ?"    " : "|   "), true, pw);
        }
    }
}