import React, { createContext, useContext, useEffect, useState } from 'react';
import '../styles/Wizard.css';
import dayjs, { Dayjs } from 'dayjs';
import Store from './Store';
import Login from './Login';
import Profile from './Profile';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';


import { FormControl, FormControlLabel, FormLabel, Radio, RadioGroup, TextField } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';
import { addPolicyToStore, createAgePolicy, createAmountPolicy, createCompositeCondition, createCompositePolicy, createDatePolicy, createHolidayPolicy, createHourPolicy, createKgPolicy, createMinAmountCondition, createMinBuyCondition, createMonthPolicy, describeBuyPolicy, describeCondition, describeDiscountPolicy, hasPermission } from '../API';
import Permission from '../models/Permission';
import RestResponse from '../models/RestResponse';
import { LocalizationProvider } from '@mui/x-date-pickers';

export const BuyPolicyWizard = () => {
    const [currentElement, setCurrentElement] = useState(<WeightPolicy />);
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
    textToElement["Restrict Product Weight"] = <WeightPolicy />
    textToElement["Restrict Product Amount"] = <AmountPolicy />
    textToElement["Restrict User Age"] = <AgePolicy />
    textToElement["Restrict By Hour"] = <HourPolicy />
    textToElement["Restrict By Jewish Customs"] = <RoshKodeshPolicy />
    textToElement["Restrict On Holiday"] = <HolidayPolicy />
    textToElement["Restrict By Date"] = <DatePolicy />
    textToElement["Composite Policy"] = <CompositePolicy />

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

const WeightPolicy = () => {
    const [product, setProduct] = useState("");
    const [minWeight, setMin] = useState("");
    const [maxWeight, setMax] = useState("");
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createKgPolicy(parseInt(product), parseFloat(maxWeight), parseFloat(minWeight))
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createKgPolicy(parseInt(product), parseFloat(maxWeight), parseFloat(minWeight))
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain product will only be allowed to be purchased within a range of weight</h3>
            <TextField type='number' size='small' id="outlined-basic" label="Product ID" variant="outlined" value={product} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setProduct(event.target.value);}} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Minimum Weight" variant="outlined" value={minWeight} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMin(event.target.value); }} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Maximum Weight" variant="outlined" value={maxWeight} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMax(event.target.value); }} />
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const AmountPolicy = () => {
    const [product, setProduct] = useState("");
    const [minAmount, setMin] = useState("");
    const [maxAmount, setMax] = useState("");
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createAmountPolicy(parseInt(product), parseInt(minAmount), parseInt(maxAmount))
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createAmountPolicy(parseInt(product), parseInt(minAmount), parseInt(maxAmount))
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain product will only be allowed to be purchased within a range of amount</h3>
            <TextField type='number' size='small' id="outlined-basic" label="Product ID" variant="outlined" value={product} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setProduct(event.target.value);}} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Minimum amount" variant="outlined" value={minAmount} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMin(event.target.value); }} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Maximum amount" variant="outlined" value={maxAmount} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMax(event.target.value); }} />
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const AgePolicy = () => {
    const [category, setCategory] = useState("");
    const [minAge, setMin] = useState("");
    const [maxAge, setMax] = useState("");
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createAgePolicy(category, parseInt(minAge), parseInt(maxAge))
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createAgePolicy(category, parseInt(minAge), parseInt(maxAge))
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain category will only be allowed to be purchased within a range of the buyer's age</h3>
            <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value);}} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Minimum age" variant="outlined" value={minAge} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMin(event.target.value); }} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Maximum age" variant="outlined" value={maxAge} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMax(event.target.value); }} />
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const HourPolicy = () => {
    const [category, setCategory] = useState("");
    const [fromTime, setMin] = useState(dayjs('2024-04-17T12:00'));
    const [toTime, setMax] = useState(dayjs('2024-04-17T12:00'));
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createHourPolicy(category, fromTime.hour(), fromTime.minute(), toTime.hour(), toTime.minute())
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createHourPolicy(category, fromTime.hour(), fromTime.minute(), toTime.hour(), toTime.minute())
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain category will only be allowed to be purchased at a certain time of day</h3>
            <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value);}} />
            <h1/>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <TimePicker label="From" value={fromTime} onChange={(newValue) => setMin(newValue!)}/>
            </LocalizationProvider>
            <h1/>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <TimePicker label="To" value={toTime} onChange={(newValue) => setMax(newValue!)}/>
            </LocalizationProvider>
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const RoshKodeshPolicy = () => {
    const [category, setCategory] = useState("");
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createMonthPolicy(category)
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createMonthPolicy(category)
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain category will not be sold at the start of the hebrew month</h3>
            <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value);}} />
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const HolidayPolicy = () => {
    const [category, setCategory] = useState("");
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createHolidayPolicy(category)
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createHolidayPolicy(category)
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain category will not be sold during holidays</h3>
            <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value);}} />
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const DatePolicy = () => {
    const [category, setCategory] = useState("");
    const [day, setDay] = useState("-1");
    const [month, setMonth] = useState("-1");
    const [year, setYear] = useState("-1");
    const {storeId} = useParams();


    const onCreate = async () => {
        let id: string = await createDatePolicy(category, parseInt(day), parseInt(month), parseInt(year))
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createDatePolicy(category, parseInt(day), parseInt(month), parseInt(year))
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    return (
        <div className='discountEditor'>
            <h3>A certain category will not be sold during a specified day, month or year</h3>
            <TextField size='small' id="outlined-basic" label="Category" variant="outlined" value={category} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setCategory(event.target.value);}} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Day" variant="outlined" value={day} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setDay(event.target.value);}} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Month" variant="outlined" value={month} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setMonth(event.target.value);}} />
            <h1/>
            <TextField type='number' size='small' id="outlined-basic" label="Year" variant="outlined" value={year} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setYear(event.target.value);}} />
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};

const CompositePolicy = () => {
    const [condId, setCondId] = useState("-1");
    const [logic, setLogic] = useState("OR");
    const [id1, setId1] = useState("0");
    const [id2, setId2] = useState("0");
    const [desc1, setDesc1] = useState("");
    const [desc2, setDesc2] = useState("");
    const {storeId} = useParams();

    const onCreate = async () => {
        let id: string = await createCompositePolicy(parseInt(id1), parseInt(id2), logic);
        alert(`Buy policy created with ID ${id}`)
    }

    const onCreateAndSave = async () => {
        let id: string = await createCompositePolicy(parseInt(id1), parseInt(id2), logic);
        alert(`Buy policy created with ID ${id}`)
        await addPolicyToStore(parseInt(storeId!), parseInt(id));
    }

    useEffect(() => {
        fetchDescriptions();
      },[id1,id2])
    const logicList = ["OR", "AND", "Conditioning"]
    const fetchDescriptions = async () =>{
        let resp1: RestResponse = await describeBuyPolicy(id1);
        let resp2: RestResponse = await describeBuyPolicy(id2);
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
            <TextField type='number' size='small' id="outlined-basic" label="ID of first discount" variant="outlined" value={id1} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setId1(event.target.value); }} />
            <p className='policyDescription'>Description: {desc1}</p>
            <TextField type='number' size='small' id="outlined-basic" label="ID of second discount" variant="outlined" value={id2} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setId2(event.target.value); }} />
            <p className='policyDescription'>Description: {desc2}</p>
            <button onClick={onCreate} className='editorButton'>Create</button>
            <button onClick={onCreateAndSave} className='editorButton'>Create and add to store</button>
        </div>
    );
};



export default BuyPolicyWizard;
