import java.util.LinkedList;
public class Assignment {
    public LinkedList<Variable>setOfSolution;

    public Assignment()
    {
        this.setOfSolution = new LinkedList<>();
    }

    public static Variable copying_variable(Variable v)
    {
        Variable v1 = new Variable(v.name);
        v1.adjacent.addAll(v.adjacent);
        v1.domain.addAll(v.domain);
        v1.chosen_color=v.chosen_color;
        return v1;
    }
    public void addSol(Variable v)
    {
        Variable v1= copying_variable(v);
        setOfSolution.add(v1);
    }
    public void removeSol(Variable v)
    {
        int size=setOfSolution.size();
        for (int i = 0; i < size ; i++)
        {
            if (setOfSolution.get(i).name.equalsIgnoreCase(v.name))
            {
                setOfSolution.remove(i);
                break;
            }
        }
    }
}
