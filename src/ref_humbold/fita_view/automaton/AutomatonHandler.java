package ref_humbold.fita_view.automaton;

import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ref_humbold.fita_view.Pair;

abstract class AutomatonHandler
    extends DefaultHandler
{
    protected Collection<String> alphabet = new ArrayList<>();
    protected Map<Integer, Variable> variables = new HashMap<>();
    protected Map<Variable, Pair<String, Boolean>> conditions;
    protected StringBuilder content;
    protected String tagName;
    protected Integer varID;
    protected boolean isDeterministic;
    private Locator locator;
    private List<String> varValues = new ArrayList<>();

    protected abstract TreeAutomaton getAutomaton();

    @Override
    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        content = new StringBuilder();

        switch(qName)
        {
            case "alphabet":
            case "variables":
            case "transitions":
            case "word":
            case "value":
                break;

            case "automaton":
                isDeterministic = Boolean.parseBoolean(attributes.getValue("determinism"));
                break;

            case "var":
                varValues = new ArrayList<>();
                varValues.add(attributes.getValue("init"));
                varID = Integer.parseInt(attributes.getValue("id"));
                break;

            case "conditions":
                conditions = new HashMap<>();
                break;

            case "trans":
                varID = Integer.parseInt(attributes.getValue("var-id"));

                if(!variables.containsKey(varID))
                    throw new NoVariableWithIDException(
                        writePosition() + "No variable with with ID " + varID + ".");
                break;

            case "accept":
                int id = Integer.parseInt(attributes.getValue("var-id"));

                if(!variables.containsKey(varID))
                    throw new NoVariableWithIDException(
                        writePosition() + "No variable with with ID " + varID + ".");

                Variable v = variables.get(id);

                if(conditions.containsKey(v))
                    throw new DuplicatedAcceptanceValueException(
                        writePosition() + "Acceptance condition for variable with ID " + id
                            + " has been already defined.");

                if(attributes.getIndex("include") >= 0 && attributes.getIndex("exclude") >= 0)
                    throw new IncorrectAcceptanceConditionException(
                        writePosition() + "Acceptance condition for variable with ID " + id
                            + "contains both \'include\' and \'exclude\'.");

                if(attributes.getIndex("include") < 0 && attributes.getIndex("exclude") < 0)
                    throw new IncorrectAcceptanceConditionException(
                        writePosition() + "Acceptance condition for variable with ID " + id
                            + "contains neither \'include\' nor \'exclude\'.");

                if(attributes.getIndex("include") >= 0)
                {
                    String value = attributes.getValue("include");

                    if(!Objects.equals(value, Wildcard.EVERY_VALUE) && !v.contains(value))
                        throw new IllegalVariableValueException(
                            writePosition() + "Given value \'" + value
                                + "\'is not a value of variable with ID " + id + ".");

                    conditions.put(v, Pair.make(value, true));
                }
                else if(attributes.getIndex("exclude") >= 0)
                {
                    String value = attributes.getValue("exclude");

                    if(!v.contains(value))
                        throw new IllegalVariableValueException(
                            writePosition() + "Given value \'" + value
                                + "\'is not a value of variable with ID " + id + ".");

                    conditions.put(v, Pair.make(value, false));
                }
                break;

            default:
                throw new AutomatonParsingException(
                    writePosition() + "Unexpected tag: \'" + qName + "\'");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        switch(qName)
        {
            case "automaton":
            case "alphabet":
            case "accept":
            case "transitions":
                break;

            case "word":
                alphabet.add(content.toString());
                content = null;
                break;

            case "value":
                varValues.add(content.toString());
                content = null;
                break;

            case "var":
                try
                {
                    variables.put(varID, new Variable(varID, varValues.get(0), varValues));
                }
                catch(IllegalVariableValueException e)
                {
                    throw new IllegalVariableValueException(
                        writePosition() + "Illegal value of variable with ID " + varID + ". "
                            + e.getMessage());
                }
                break;

            default:
                throw new AutomatonParsingException(
                    writePosition() + "Unexpected tag: \'" + qName + "\'");
        }
    }

    @Override
    public void characters(char[] chars, int start, int length)
    {
        content.append(chars, start, length);
    }

    @Override
    public void error(SAXParseException e)
        throws SAXException
    {
        throw new AutomatonParsingException(e.getMessage(), e);
    }

    protected String writePosition()
    {
        if(locator == null)
            return "";

        return "LINE " + locator.getLineNumber() + ", COLUMN " + locator.getColumnNumber() + ": ";
    }
}
