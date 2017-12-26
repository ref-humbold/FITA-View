package ref_humbold.fita_view.automaton;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class TopDownAutomatonHandler
    extends AutomatonHandler
{
    private TopDownFiniteTreeAutomaton automaton;
    private boolean isDeterministic;
    private String nodeValue;
    private String label;
    private String leftResult;
    private String rightResult;

    @Override
    public TreeAutomaton getAutomaton()
    {
        return automaton;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        tagName = qName;
        content = new StringBuilder();

        switch(qName)
        {
            case "automaton":
                isDeterministic = Boolean.parseBoolean(attributes.getValue("determinism"));
                break;

            case "label":
            case "node-value":
            case "left-result":
            case "right-result":
                break;

            default:
                super.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        switch(qName)
        {
            case "variables":
                automaton = isDeterministic ? new TopDownDFTA(alphabet, variables.values())
                                            : new TopDownNFTA(alphabet, variables.values());
                break;

            case "trans":
                automaton.addTransition(variables.get(varID), nodeValue, label, leftResult,
                                        rightResult);
                break;

            case "accept":
                for(Integer id : variables.keySet())
                {
                    if(accept.get(variables.get(id)) == null)
                        throw new NoAcceptingForVariableException(
                            "Variable with ID " + id + "has no accepting value.");
                }

                automaton.addAcceptingState(accept);
                break;

            case "label":
                label = content.toString();
                content = null;

                if(!label.equals(Wildcard.EVERY_VALUE) && !alphabet.contains(label))
                    throw new IllegalAlphabetWordException(
                        "Given label \'" + label + "\' is not a part of automaton's alphabet.");
                break;

            case "node-value":
                nodeValue = content.toString();
                content = null;

                if(!nodeValue.equals(Wildcard.EVERY_VALUE) && !variables.get(varID)
                                                                        .contains(nodeValue))
                    throw new IllegalVariableValueException("Given node-value  \'" + nodeValue
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                break;

            case "left-result":
                leftResult = content.toString();
                content = null;

                if(!leftResult.equals(Wildcard.SAME_VALUE) && !variables.get(varID)
                                                                        .contains(leftResult))
                    throw new IllegalVariableValueException("Given left-result \'" + leftResult
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                break;

            case "right-result":
                rightResult = content.toString();
                content = null;

                if(!rightResult.equals(Wildcard.SAME_VALUE) && !variables.get(varID)
                                                                         .contains(rightResult))
                    throw new IllegalVariableValueException("Given right-result \'" + rightResult
                                                                + "\'is not a value of variable with ID "
                                                                + varID + ".");
                break;

            default:
                super.endElement(uri, localName, qName);
        }
    }
}
