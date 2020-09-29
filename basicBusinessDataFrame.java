/**
 * General information class intended for storing data from a initial business search
 */
public class basicBusinessDataFrame {

	public String[] data;
	
	basicBusinessDataFrame(String[] _data)
	{
		data = new String[_data.length];
		
		for (int i = 0; i < _data.length; ++i)
		{
			data[i] = _data[i];
		}
	}

	String getColumn(int column)
	{
		return data[column];
	}
}
