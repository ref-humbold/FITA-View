package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

abstract class AutomatonHandler
    extends DefaultHandler
{
    protected Collection<String> alphabet = new ArrayList<>();
    protected Map<Integer, Variable> variables = new HashMap<>();
    protected Map<Variable, String> accept = new HashMap<>();
    protected StringBuilder content = null;
    protected String tagName = null;
    protected Integer varID;
    private List<String> varValues = new ArrayList<>();

    protected abstract TreeAutomaton getAutomaton();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        content = new StringBuilder();

        switch(qName)
        {
            case "alphabet":
            case "variables":
            case "accepting":
            case "transitions":
            case "word":
            case "value":
                break;

            case "var":
                varValues = new ArrayList<>();
                varValues.add(attributes.getValue("init"));
                varID = Integer.parseInt(attributes.getValue("id"));
                break;

            case "trans":
                varID = Integer.parseInt(attributes.getValue("var-id"));

                if(!variables.containsKey(varID))
                    throw new NoVariableWithIDException("No variable with with ID " + varID + ".");
                break;

            case "accept":
                accept.clear();
                break;

            case "var-acc":
                int id = Integer.parseInt(attributes.getValue("var-id"));
                String value = attributes.getValue("value");

                if(!variables.containsKey(varID))
                    throw new NoVariableWithIDException("No variable with with ID " + varID + ".");

                Variable v = variables.get(id);

                if(!value.equals(Wildcard.EVERY_VALUE) && !v.contains(value))
                    throw new IllegalVariableValueException(
                        "Given accepting value \'" + value + "\'is not a value of variable with ID "
                            + id + ".");

                if(accept.containsKey(v))
                    throw new DuplicatedAcceptingValueException(
                        "Accepting value for variable with ID " + id
                            + " has been already defined.");

                accept.put(v, value);
                break;

            default:
                throw new AutomatonParsingException("Unexpected tag: \'" + qName + "\'");
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
            case "accepting":
            case "var-acc":
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
                    variables.put(varID, new Variable(varValues.get(0), varValues));
                }
                catch(IllegalVariableValueException e)
                {
                    throw new IllegalVariableValueException(
                        "Illegal value of variable with ID " + varID + ". " + e.getMessage());
                }
                break;

            default:
                throw new AutomatonParsingException("Unexpected tag: \'" + qName + "\'");
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
}
