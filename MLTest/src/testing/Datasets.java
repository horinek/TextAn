package testing;

import java.util.SortedSet;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.InstanceTools;


import org.junit.Assert;

public class Datasets {
	 public static void main(String[]args){
	        Dataset data = new DefaultDataset();
	        for (int i = 0; i < 10; i++) {
	            Instance tmpInstance = InstanceTools.randomInstance(25);
	            data.add(tmpInstance);
	        }
	        /* Retrieve all class values that are ever used in the data set */
	        SortedSet<Object> classValues = data.classes();
	        

	        Assert.assertEquals(data.noAttributes(), 25);
	        Assert.assertEquals(data.size(), 10);
	        Assert.assertEquals(classValues.size(), 0);

	    }
}
