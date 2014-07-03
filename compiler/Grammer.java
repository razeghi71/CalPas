/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mohammad
 */
public class Grammer {

	ArrayList<Rule> rules = new ArrayList<Rule>();
	public Grammer(String filePath) throws IOException {
		readFromFile(filePath);
		//		calcFirst();
		calcFollow();

	}

	public Grammer()
	{

	}

	public void parseFile (String filePath)
	{

	}


	public Set<Terminal> getFirsts(NonTerminal A)
	{
		Set<Terminal> result=new HashSet<>();
		ArrayList<Token> rhs;
		NonTerminal lhs;
		Set<Terminal> temp;

		for(int counter = 0;counter < rules.size(); counter++ )
		{
			lhs=rules.get(counter).getLeftSide();
			rhs=rules.get(counter).getRightSide();
			for(int i = rhs.size()-1;i>-1;i--)
			{
				if(rhs.get(i).getName().charAt(0) == '#')
					rhs.remove(i);
			}

			if(lhs.getName().equals(A.getName()))
			{
				if(Terminal.isTerminal(rhs.get(0).getName()))
				{
					result.add((Terminal)rhs.get(0));
				}
				else
				{
					int i = 0;
					temp = getFirsts((NonTerminal)rhs.get(0));
					if(rhs.size() > 1)
						temp.remove(Terminal.getTerminal("Epsilon"));
					result.addAll(temp);

					while(getFirsts((NonTerminal)rhs.get(i)).contains(Terminal.getTerminal("Epsilon")) && 
							i < rhs.size())
					{
						if(Terminal.isTerminal(rhs.get(i+1).getName())){
							result.add((Terminal)rhs.get(i+1));
							break;
						}
						if(rhs.size() == i+1)
							result.addAll(getFirsts((NonTerminal)rhs.get(i+1)));
						else
						{
							temp=getFirsts((NonTerminal)rhs.get(i+1));
							temp.remove(Terminal.getTerminal("Epsilon"));
							result.addAll(temp);
						}
					}

					i++;
				}
			}

		}

		A.firsts.addAll(result);
		return result;


	}

	public Set<Terminal> getFirsts(ArrayList<Token> A)
	{
		Set<Terminal> result = new HashSet<>();
		if(Terminal.isTerminal(A.get(0).getName()))
		{
			result.add((Terminal)A.get(0));
			return result;
		}
		Set<Terminal> firsts_of_first_element = getFirsts((NonTerminal)A.get(0));
		Set<Terminal> firsts_of_the_others = new HashSet<>();
		ArrayList<Token> the_others = new ArrayList<>();

		if(A.size() > 1)
		{
			the_others = (ArrayList<Token>)the_others.subList(1, A.size() - 1);
		}
		firsts_of_the_others = getFirsts(the_others);

		result.addAll(firsts_of_first_element);

		if(result.contains(Terminal.getTerminal("Epsilon")) && A.size() > 1)
		{
			result.remove(Terminal.getTerminal("Epsilon"));
			result.addAll(firsts_of_the_others);
		}

		return result;
	}

	public Set<Terminal> getFollows(NonTerminal A)
	{
		NonTerminal lhs;
		ArrayList<Token> rhs;
		ArrayList<Token> temp;
		Set<Terminal> result = new HashSet<Terminal>();
		ArrayList<Token> after_A = new ArrayList<>();
		int[] index;
		int iter=0;

		for(int counter = 0 ; counter < rules.size() ; counter++)
		{
			lhs = rules.get(counter).getLeftSide();
			rhs =  rules.get(counter).getRightSide();
			temp=rhs;
			
			for(int i = rhs.size()-1;i>-1;i--)
			{
				if(rhs.get(i).getName().charAt(0) == '#')
					rhs.remove(i);
			}
			
			
			index[iter] = rhs.indexOf((Token)A);
			while(index[iter] != -1 && index[iter] < rhs.size()-1)
			{
				temp.clear();
				for(int ii = index[iter]+1; ii < rhs.size() ;ii++)
					temp.add((Token)rhs.get(ii));
				iter++;
				index[iter] = temp.indexOf((Token)A);
			}
			
			

			if(index != -1)
			{
				if(index == rhs.size() - 1)
					result.addAll(getFollows(lhs));
				else
				{
					for(int ii = index+1; ii < rhs.size() ;ii++)
						after_A.add((Token)rhs.get(ii));
					
					result.addAll(getFirsts(after_A));
					if(getFirsts(after_A).contains(Terminal.getTerminal("Epsilon")))
					{
						result.remove(Terminal.getTerminal("Epsilon"));
						result.addAll(getFollows(lhs));
					}
				}
			}



		}

		A.follows.addAll(result);
		return result;

	}

	public void calcFirst()
	{

		Set<Terminal> set = getFirsts(NonTerminal.getNonTerminal("programme"));
		for(Terminal s : set)
			System.out.println(s.getName());
		System.out.println("kir");
		set=getFirsts(NonTerminal.getNonTerminal("arguments"));
		for(Terminal s : set)
			System.out.println(s.getName());


	}

	public void calcFollow()
	{
		Set<Terminal> set = getFollows(NonTerminal.getNonTerminal("programme"));
		for(Terminal s : set)
			System.out.println(s.getName());
		System.out.println("kir");
		set=getFollows(NonTerminal.getNonTerminal("arguments"));
		for(Terminal s : set)
			System.out.println(s.getName());
	}


	private void readFromFile(String filePath) throws IOException
	{
		BufferedReader reader=new BufferedReader(new FileReader(filePath));
		int number_of_ids;
		int number_of_rules;
		String[] tokenized;


		String line=null;

		line=reader.readLine();

		//we will read the used IDs and their Regex
		number_of_ids=Integer.parseInt(line);
		for(int counter=0;counter<number_of_ids;counter++)
		{
			line=reader.readLine();
			tokenized=line.split("@");
			if(tokenized.length>1)
				Terminal.addTerminal(tokenized[0],tokenized[1]);

			else
				Terminal.addTerminal(tokenized[0],"");
		}

		line=reader.readLine();

		//we will read grammars rules and save them
		number_of_rules=Integer.parseInt(line);
		for(int counter=0; counter< number_of_rules;counter++)
		{
			line=reader.readLine();
			rules.add(new Rule(line));

		}


	}



}
