import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MvcPatternTest {

	public static final int FIRST_TEST_VALUE = 100;
	public static final int SECOND_TEST_VALUE = 200;

	static class Model {
		int val;

		public int getVal() {
			return val;
		}
		public void setVal(int val) {
			this.val = val;
		}
	}

	class View {
		public void printModel(Model model) {
			System.out.println("Model val : " + model.getVal());
		}
	}

	class Controller {
		Model model;
		View view;

		public Controller(Model model, View view) {
			this.model = model;
			this.view = view;
		}

		public void setModelVal(int val) {
			model.setVal(val);
		}

		public int getModelVal() {
			return model.getVal();
		}

		public void updateView() {
			view.printModel(model);
		}
	}

	static class Util {
		public static Model getModelFromDB() {
			Model model = new Model();
			model.setVal(FIRST_TEST_VALUE);
			return model;
		}
	}

	@Test
	public void mvcPatternTest() {
		Model model = Util.getModelFromDB();
		View view = new View();

		Controller controller = new Controller(model, view);
		controller.updateView();
		assertThat(controller.getModelVal()).isEqualTo(FIRST_TEST_VALUE);

		controller.setModelVal(SECOND_TEST_VALUE);
		controller.updateView();
		assertThat(controller.getModelVal()).isEqualTo(SECOND_TEST_VALUE);
	}
}
