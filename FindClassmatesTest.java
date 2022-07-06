import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


/*
 * This class includes test cases for the basic/normal functionality of the 
 * FriendFinder.findClassmates method, but does not check for any error handling.
 */

public class FindClassmatesTest {
	
	protected FriendFinder ff;
		
	protected ClassesDataSource defaultClassesDataSource = new ClassesDataSource() {

		@Override
		public List<String> getClasses(String studentName) {

			if (studentName.equals("A")) {
				return List.of("1", "2", "3");
			}
			else if (studentName.equals("B")) {
				return List.of("1", "2", "3");
			}
			else if (studentName.equals("C")) {
				return List.of("2", "4");
			}
			else return null;			
		
		}
		
	};
	
	protected StudentsDataSource defaultStudentsDataSource = new StudentsDataSource() {

		@Override
		public List<Student> getStudents(String className) {
			
			Student a = new Student("A", 101);
			Student b = new Student("B", 102);
			Student c = new Student("C", 103);

			if (className.equals("1")) {
				return List.of(a, b);
			}
			else if (className.equals("2")) {
				return List.of(a, b, c);
			}
			else if (className.equals("3")) {
				return List.of(a, b);
			}
			else if (className.equals("4")) {
				return List.of(c);
			}
			else return null;
		}
		
	};
	

	@Test
	public void testFindOneFriend() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));

	}

	@Test
	public void testFindNoFriends() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("C", 103));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}
	
	@Test
	public void testClassesDataSourceReturnsNullForInputStudent() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("D", 104));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStudentArg() {
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testNullClassesDataSource() {
		ff = new FriendFinder(null, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
	}

	@Test(expected = IllegalStateException.class)
	public void testNullStudentsDataSource() {
		ff = new FriendFinder(defaultClassesDataSource, null);
		Set<String> response = ff.findClassmates(new Student("A", 101));
	}

	@Test
	public void testNullClassmateName() {
		StudentsDataSource mockStudentsDataSource = new StudentsDataSource() {

			@Override
			public List<Student> getStudents(String className) {

				Student a = new Student(null, 102);
				Student b = new Student("B", 103);

				return List.of(a,b);
			}

		};

		ff = new FriendFinder(defaultClassesDataSource, mockStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));
	}

	@Test
	public void testStudentDataSourceReturnsNullForGivenClass() {

		ClassesDataSource newClassesDataSource = new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {

				return List.of("1", "2");

			}

		};

		StudentsDataSource newStudentsDataSource = new StudentsDataSource() {

			@Override
			public List<Student> getStudents(String className) {

				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);

				if (className.equals("1")) {
					return null;
				}
				else if (className.equals("2")) {
					return List.of(a, b, c);
				}
				else return null;
			}

		};

		ff = new FriendFinder(newClassesDataSource, newStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(2, response.size());
		assertTrue(response.contains("B"));
		assertTrue(response.contains("C"));
	}

	@Test
	public void testClassmateHasNullClasses() {

		ClassesDataSource newClassesDataSource = new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {

				if (studentName.equals("A") || studentName.equals("C")) {
					return List.of("1", "2");
				}
				else {
					return null;
				}

			}

		};

		StudentsDataSource newStudentsDataSource = new StudentsDataSource() {

			@Override
			public List<Student> getStudents(String className) {

				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);

				return List.of(a, b, c);
			}

		};

		ff = new FriendFinder(newClassesDataSource, newStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("C"));
	}
}
