
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FriendFinder {
	
	protected ClassesDataSource classesDataSource;
	protected StudentsDataSource studentsDataSource;
	
	public FriendFinder(ClassesDataSource cds, StudentsDataSource sds) {
		classesDataSource = cds;
		studentsDataSource = sds;
	}
	
	/*
	 * This method takes a String representing the name of a student 
	 * and then returns a set containing the names of everyone else 
	 * who is taking the same classes as that student.
	 */
	public Set<String> findClassmates(Student theStudent) {

		if (theStudent == null) {
			throw new IllegalArgumentException("Null Student argument input");
		}

		String name = theStudent.getName();
		
		// find the classes that this student is taking
		if (classesDataSource == null) {
			throw new IllegalStateException("Null classes data source");
		}
		List<String> myClasses = classesDataSource.getClasses(name);
		if (myClasses == null) {
			return Collections.emptySet(); // return empty Set if the student isn't taking any classes
		}
		
		// use the classes to find the names of the students
		Set<String> classmates = new HashSet<String>();
		
		for (String myClass : myClasses) {
			// list all the students in the class
			if (studentsDataSource == null) {
				throw new IllegalStateException("Null students data source");
			}
			List<Student> students = studentsDataSource.getStudents(myClass);
			if (students == null) {
				continue; // skip this class if there are no other students
			}
			for (Student otherStudent : students) {
				
				// find the other classes that they're taking
				if (otherStudent.getName() == null) {
					continue; // skip nameless students
				}
				List<String> theirClasses = classesDataSource.getClasses(otherStudent.getName());

							
				// see if all of the classes that they're taking are the same as the ones this student is taking
				boolean allSame = true;

				if (theirClasses == null) {
					continue; // if another student has no classes, then they aren't classmates
				}

				for (String c : myClasses) {
					if (theirClasses.contains(c) == false) {
						allSame = false;
						break;
					}
				}
				
				// if they're taking all the same classes, then add to the set of classmates

				if (allSame) {
					if (otherStudent.getName().equals(name) == false) {
						classmates.add(otherStudent.getName());
					}
				}
			}

		}
				
		return classmates;
	}
	

}
