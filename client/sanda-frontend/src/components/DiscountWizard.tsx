import React, { useState } from 'react';
import '../styles/Wizard.css';
import Store from './Store';
import Login from './Login';
import Profile from './Profile';
import { FormControl, FormControlLabel, FormLabel, Radio, RadioGroup, TextField } from '@mui/material';

export const DiscountWizard = () => {
    const [currentElement, setCurrentElement] = useState(<SimpleDiscount />);

    interface Dictionary<T> {
        [Key: string]: T;
    }

    let textToElement: Dictionary<JSX.Element> = {}
    textToElement["Simple Discount"] = <SimpleDiscount />
    textToElement["Condition Discount"] = <ConditionDiscuont />

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
            <p>A simple discount simply applies a given percentage on a given product, category or entire store</p>
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

const ConditionDiscuont = () => {
    const [percentage, setPercentage] = useState("0");
    const [mode, setMode] = useState("store");
    const [category, setCategory] = useState("");
    const [product, setProduct] = useState("");

    return (
        <div className='discountEditor'>
            <p>A simple discount simply applies a given percentage on a given product, category or entire store only if a condition applies</p>
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
            <ConditionWizard/>
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
            <button className='editorButton'>Create Condition</button>
            <button className='editorButton'>Create Condition and use for discount</button>
        </div>
    );
};

const BuyCondition = () => {
    const [buy, setBuy] = useState("0");

    return (
        <div className='discountEditor'>
            <p>A discount with this condition will only apply if the total price of a purchase is above a minimum</p>
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Minimum" variant="outlined" value={buy} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setBuy(event.target.value); }} />
            <button className='editorButton'>Create Condition</button>
            <button className='editorButton'>Create Condition and use for discount</button>
        </div>
    );
};


export default DiscountWizard;
