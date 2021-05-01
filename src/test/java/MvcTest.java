import org.junit.jupiter.api.Test;

public class MvcTest {
	class Model {
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

		public void updateView() {
			view.printModel(model);
		}
	}

	@Test
	public void mvcPatternTest() {

	}
}
