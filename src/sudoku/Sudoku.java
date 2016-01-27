/**
 * 
 */
package sudoku;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

/**
 * @author damvantai
 *
 */
public class Sudoku {

	public void solve()
	{
		LocalSearchManager ls = new LocalSearchManager();
		VarIntLS[][] x = new VarIntLS[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
				x[i][j] = new VarIntLS(ls, 1, 9);
		}
		
		
		
		// check constraint on row
		ConstraintSystem S = new ConstraintSystem(ls);
		for (int i = 0; i < 9; i++)
		S.post(new AllDifferent(x[i]));
		
		
		// check constraint on column
		VarIntLS[][] y = new VarIntLS[9][9];
		for (int i = 0; i < 9; i++)
			for (int j =0; j < 9; j++)
				y[i][j] = x[j][i];

		for (int i = 0; i < 9; i++)
			S.post(new AllDifferent(y[i]));

		
		// check constraint on square 3 * 3 child
		VarIntLS[][] a = new VarIntLS[9][9];	
		int m = 0;
		int n = 0;
		for (int k = 0; k < 3; k++)
		{
			for (int h = 0; h < 3; h++)
			{
				n = 0;
				for (int i = k * 3; i < 3 * (1 + k); i++)
				{
					for (int j = h * 3; j < (h + 1)*3; j++)
					{
						a[m][n] = x[i][j];
						n++;
					}
				}
				m++;
			}
		}
		for (int i = 0; i < 9; i++)
			S.post(new AllDifferent(a[i]));
	
		

		ls.close();

		MinMaxSelector mms = new MinMaxSelector(S);
		int it = 0;
		while (it < 10000 && S.violations() > 0 )
		{
			VarIntLS sel_x = mms.selectMostViolatingVariable();
			int sel_v = mms.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(sel_v);
			it++;
			System.out.println("Step " + it + ", violations = " + S.violations());
		}
		
//		System.out.println(x);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sudoku S = new Sudoku();
		S.solve();

	}

}
