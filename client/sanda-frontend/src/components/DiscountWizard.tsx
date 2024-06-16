import React, { createContext, useContext, useEffect, useState } from 'react';
import '../styles/Wizard.css';
import Store from './Store';
import Login from './Login';
import Profile from './Profile';
import { FormControl, FormControlLabel, FormLabel, Radio, RadioGroup, TextField } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';
import { createCompositeCondition, createMinAmountCondition, createMinBuyCondition, describeCondition, describeDiscountPolicy, hasPermission } from '../API';
import Permission from '../models/Permission';
import RestResponse from '../models/RestResponse';

const ConditionContext = createContext({
    condId: '-1',
    setCondId: (cond: string) => {}
});

export const DiscountWizard = () => {
    const [currentElement, setCurrentElement] = useState(<SimpleDiscount />);
    const {storeId} = useParams();
    const navigate = useNavigate();
    interface Dictionary<T> {
        [Key: string]: T;
    }

    useEffect(() => {
        const checkAllowed = async ()=> {
            let canAccess: boolean = await hasPermission(storeId!, Permission.ADD_BUY_POLICY);
            if(!canAccess){
                navigate('/permission-error', {state: "You do not have Edit Buy Policies permission in the given store"})
            }
        }
        checkAllowed();
    }, [])

    let textToElement: Dictionary<JSX.Element> = {}
    textToElement["Simple Discount"] = <SimpleDiscount />
    textToElement["Condition Discount"] = <ConditionDiscount />
    textToElement["Composite Discount"] = <CompositeDiscount />

    return (
        <div className="wizard">
            <div className="selector">
                {Object.keys(textToElement).map(buttontext => <button onClick={() => setCurrentElement(textToElement[buttontext])} className='selectorbutton'>{buttontext}</button>)}
            </div>
            <div className='element'>
                {currentElement}
            </div>
        </div>
    );
};

const SimpleDiscount = () => {
    const [percentage, setPercentage] = useState("0");
    const [mode, setMode] = useState("store");
    const [category, setCategory] = useState("");
    const [product, setProduct] = useState("");

    return (
        <div className='discountEditor'>
            <h3>A simple discount simply applies a given percentage on a given product, category or entire store</h3>
            <FormControl>
                <FormLabel id="group-label">Applies on:</FormLabel>
                <RadioGroup
                    aria-labelledby="group-label"
                    defaultValue={"store"}

                    value={mode}
                    onChange={(e,v) => setMode(v)}
                    name="radio-buttons-group"
                >
                    <FormControlLabel value={"store"} control={<Radio />} label="Whole Store" />
                    <FormControlLabel value={"categ"} control={<Radio />} label="Category" />
                    <FormControlLabel value={"produ"} control={<Radio />} label="Product" />
                </RadioGroup>
            </FormControl>
            {mode === 'categ' &&
                        <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value); }} />
            }
            {mode === 'produ' &&
                        <TextField type='number' size='small' id="outlined-basic" label="Product ID" variant="outlined" value={product} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setProduct(event.target.value);}} />
            }
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Percentage" variant="outlined" value={percentage} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setPercentage(event.target.value); }} />
            <button className='editorButton'>Create</button>
            <button className='editorButton'>Create and add to store</button>
        </div>
    );
};

const ConditionDiscount = () => {
    const [percentage, setPercentage] = useState("0");
    const [mode, setMode] = useState("store");
    const [category, setCategory] = useState("");
    const [product, setProduct] = useState("");
    const [condId, setCondId] = useState("-1");
    const value = { condId, setCondId };

    return (
        <div className='discountEditor'>
            <h3>A conditioned discount applies a given percentage on a given product, category or entire store only if a condition applies</h3>
            <h4>You can use the following sub-wizard to create and select a condition</h4>
            <p>{(condId === "-1") ? "You have not selected a condition yet" : `You have selected condition with ID ${condId}`}</p>
            <ConditionContext.Provider value={value}>
                <ConditionWizard/>
            </ConditionContext.Provider>
            <FormControl>
                <FormLabel id="group-label">Discount Applies on:</FormLabel>
                <RadioGroup
                    aria-labelledby="group-label"
                    defaultValue={"store"}

                    value={mode}
                    onChange={(e,v) => setMode(v)}
                    name="radio-buttons-group"
                >
                    <FormControlLabel value={"store"} control={<Radio />} label="Whole Store" />
                    <FormControlLabel value={"categ"} control={<Radio />} label="Category" />
                    <FormControlLabel value={"produ"} control={<Radio />} label="Product" />
                </RadioGroup>
            </FormControl>
            {mode === 'categ' &&
                        <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value); }} />
            }
            {mode === 'produ' &&
                        <TextField type='number' size='small' id="outlined-basic" label="Product ID" variant="outlined" value={product} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setProduct(event.target.value);}} />
            }
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Percentage" variant="outlined" value={percentage} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setPercentage(event.target.value); }} />
            <button className='editorButton'>Create</button>
            <button className='editorButton'>Create and add to store</button>
        </div>
    );
};

const ConditionWizard = () => {
    const [currentElement, setCurrentElement] = useState(<AmountCondition />);

    interface Dictionary<T> {
        [Key: string]: T;
    }

    let textToElement: Dictionary<JSX.Element> = {}
    textToElement["Minimum Product Amount"] = <AmountCondition />
    textToElement["Minumum Total Price"] = <BuyCondition />
    textToElement["Composite Condition"] = <CompositeCondition />

    return (
        <div className="wizard">
            <div className="selector">
                {Object.keys(textToElement).map(buttontext => <button onClick={() => setCurrentElement(textToElement[buttontext])} className='selectorbutton'>{buttontext}</button>)}
            </div>
            <div className='element'>
                {currentElement}
            </div>
        </div>
    );
};

const AmountCondition = () => {
    const [amount, setAmount] = useState("0");
    const [mode, setMode] = useState("store");
    const [category, setCategory] = useState("");
    const [product, setProduct] = useState("");
    const { condId, setCondId } = useContext(ConditionContext);
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createMinAmountCondition(parseInt(amount), mode, parseInt(product), category)
        alert(`Condition created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createMinAmountCondition(parseInt(amount), mode, parseInt(product), category)
        alert(`Condition created with ID ${id}`)
        setCondId(id)
    }

    return (
        <div className='discountEditor'>
            <p>A discount with this condition will only apply if the amount of a given product, products from a certain category, or products in total are above a minimum amount</p>
            <FormControl>
                <FormLabel id="group-label">Applies on:</FormLabel>
                <RadioGroup
                    aria-labelledby="group-label"
                    defaultValue={"store"}

                    value={mode}
                    onChange={(e,v) => setMode(v)}
                    name="radio-buttons-group"
                >
                    <FormControlLabel value={"store"} control={<Radio />} label="Whole Store" />
                    <FormControlLabel value={"categ"} control={<Radio />} label="Category" />
                    <FormControlLabel value={"produ"} control={<Radio />} label="Product" />
                </RadioGroup>
            </FormControl>
            {mode === 'categ' &&
                        <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value); }} />
            }
            {mode === 'produ' &&
                        <TextField type='number' size='small' id="outlined-basic" label="Product ID" variant="outlined" value={product} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setProduct(event.target.value);}} />
            }
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Minimum Amount" variant="outlined" value={amount} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setAmount(event.target.value); }} />
            <button onClick = {onCreate} className='editorButton'>Create Condition</button>
            <button onClick = {onCreateAndSave}className='editorButton'>Create Condition and use for discount</button>
        </div>
    );
};

const BuyCondition = () => {
    const [buy, setBuy] = useState("0");
    const { condId, setCondId } = useContext(ConditionContext);

    const onCreate = async () => {
        let id: string = await createMinBuyCondition(parseInt(buy))
        alert(`Condition created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createMinBuyCondition(parseInt(buy))
        alert(`Condition created with ID ${id}`)
        setCondId(id)
    }

    return (
        <div className='discountEditor'>
            <p>A discount with this condition will only apply if the total price of a purchase is above a minimum</p>
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Minimum" variant="outlined" value={buy} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setBuy(event.target.value); }} />
            <button onClick = {onCreate} className='editorButton'>Create Condition</button>
            <button onClick = {onCreateAndSave}className='editorButton'>Create Condition and use for discount</button>
        </div>
    );
};

const CompositeDiscount = () => {
    const [condId, setCondId] = useState("-1");
    const [logic, setLogic] = useState("OR");
    const [decide, setDecide] = useState("MAX");
    const [id1, setId1] = useState("0");
    const [id2, setId2] = useState("0");
    const [desc1, setDesc1] = useState("");
    const [desc2, setDesc2] = useState("");
    const value = { condId, setCondId };

    useEffect(() => {
        fetchDescriptions();
      },[id1,id2])
    const logicList = ["OR", "AND", "XOR", "Addition", "Maximum"]
    const fetchDescriptions = async () =>{
        let resp1: RestResponse = await describeDiscountPolicy(id1);
        let resp2: RestResponse = await describeDiscountPolicy(id2);
        if(resp1.error){
            setDesc1(`Error: ${resp1.errorString}`)
        }else{
            setDesc1(resp1.dataJson)
        }
        if(resp2.error){
            setDesc2(`Error: ${resp2.errorString}`)
        }else{
            setDesc2(resp2.dataJson)
        }
    }

    return (
        <div className='discountEditor'>
            <h3>A composite discount applies a discount based on two existing discounts, based on various logical operators</h3>
            <FormControl>
                <FormLabel id="group-label">Combination logic:</FormLabel>
                <RadioGroup
                    aria-labelledby="group-label"
                    defaultValue={"OR"}

                    value={logic}
                    onChange={(e,v) => setLogic(v)}
                    name="radio-buttons-group"
                >
                    {logicList.map(logi => <FormControlLabel value={logi} control={<Radio />} label={logi} />)}
                </RadioGroup>
            </FormControl>
            <h1/>
            {logic === "XOR" && 
            <FormControl>
            <FormLabel id="group-label">Decision logic:</FormLabel>
            <RadioGroup
                aria-labelledby="group-label"
                defaultValue={"MAX"}

                value={logic}
                onChange={(e,v) => setDecide(v)}
                name="radio-buttons-group"
            >
                <FormControlLabel value={"MAX"} control={<Radio />} label={"MAX"} />
                <FormControlLabel value={"MIN"} control={<Radio />} label={"MIN"} />
            </RadioGroup>
        </FormControl>}
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="ID of first discount" variant="outlined" value={id1} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setId1(event.target.value); }} />
            <p className='policyDescription'>Description: {desc1}</p>
            <TextField type='number' size='small' id="outlined-basic" label="ID of second discount" variant="outlined" value={id2} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setId2(event.target.value); }} />
            <p className='policyDescription'>Description: {desc2}</p>
            <button className='editorButton'>Create</button>
            <button className='editorButton'>Create and add to store</button>
        </div>
    );
};

const CompositeCondition = () => {
    const { condId, setCondId } = useContext(ConditionContext);
    const [logic, setLogic] = useState("OR");
    const [id1, setId1] = useState("0");
    const [id2, setId2] = useState("0");
    const [desc1, setDesc1] = useState("");
    const [desc2, setDesc2] = useState("");
    const value = { condId, setCondId };

    useEffect(() => {
        fetchDescriptions();
      },[id1,id2])
    const logicList = ["OR", "AND", "XOR"]
    const fetchDescriptions = async () =>{
        let resp1: RestResponse = await describeCondition(id1);
        let resp2: RestResponse = await describeCondition(id2);
        if(resp1.error){
            setDesc1(`Error: ${resp1.errorString}`)
        }else{
            setDesc1(resp1.dataJson)
        }
        if(resp2.error){
            setDesc2(`Error: ${resp2.errorString}`)
        }else{
            setDesc2(resp2.dataJson)
        }
    }

    const onCreate = async () => {
        let id: string = await createCompositeCondition(parseInt(id1), parseInt(id2), logic)
        alert(`Condition created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createCompositeCondition(parseInt(id1), parseInt(id2), logic)
        alert(`Condition created with ID ${id}`)
        setCondId(id)
    }

    return (
        <div className='discountEditor'>
            <h3>A composite condition is a combination of two existing conditions based on XOR, OR or AND logic</h3>
            <FormControl>
                <FormLabel id="group-label">Combination logic:</FormLabel>
                <RadioGroup
                    aria-labelledby="group-label"
                    defaultValue={"OR"}

                    value={logic}
                    onChange={(e,v) => setLogic(v)}
                    name="radio-buttons-group"
                >
                    {logicList.map(logi => <FormControlLabel value={logi} control={<Radio />} label={logi} />)}
                </RadioGroup>
            </FormControl>
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="ID of first condition" variant="outlined" value={id1} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setId1(event.target.value); }} />
            <p className='policyDescription'>Description: {desc1}</p>
            <TextField type='number' size='small' id="outlined-basic" label="ID of second condition" variant="outlined" value={id2} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setId2(event.target.value); }} />
            <p className='policyDescription'>Description: {desc2}</p>
            <button onClick = {onCreate} className='editorButton'>Create Condition</button>
            <button onClick = {onCreateAndSave} className='editorButton'>Create Condition and use for discount</button>
        </div>
    );
};


export default DiscountWizard;
