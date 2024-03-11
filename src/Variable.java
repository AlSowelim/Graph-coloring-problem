import java.util.LinkedList;
import java.util.PriorityQueue;
public class Variable {
        public String name;
        public  LinkedList <Variable> adjacent;
        public PriorityQueue<String> domain;
        public String chosen_color;

        public Variable(String name) {
                this.name = name;
                this.adjacent=new LinkedList<>();
                this.domain=new PriorityQueue<>();
                this.chosen_color=null;
        }
}
