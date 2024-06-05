import { createContext } from "react";
import StoreModel from "./models/StoreModel"
import RestResponse from "./models/RestResponse";
import ProductModel from "./models/ProductModel";
import Permission from "./models/Permission";

export var globalToken = "";
export var globalUsername = "";

export const login = (username: string, password: string) => {
    ///request REST to login...
    let resp: RestResponse = {dataJson: "thisIsWhereTheTokenWouldBe", error: false, errorString: ""}
    if(!resp.error){
        globalToken = resp.dataJson;
        globalUsername = username;
    }
}

export const getStoreInfo = (storeId: string): StoreModel => {
    let defaultExample: StoreModel = {founderUsername: "mrOwnerMan", storeId: 0, storeName: "TestStore", email: "coolio@gmail.com", address: "Be'er Sheva, Reger Street 78", rank: 3, phoneNumber: "052-0520520", closingHours: 0, openingHours: 0}
    return defaultExample;
}

export const getStoreProducts = (storeId: string): ProductModel[] => {
    let defaultExample1: ProductModel = {name: "Example1", price: 123.3}
    let defaultExample2: ProductModel = {name: "Example2", price: 3.5}
    let list: ProductModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    return list;
}

export const searchAndFilterStoreProducts = (storeId: string, category: string, keywords: string, minprice: number, maxprice: number): ProductModel[] => {
    let defaultExample1: ProductModel = {name: "Example1", price: 123.3}
    let defaultExample2: ProductModel = {name: "Example2", price: 3.5}
    let list: ProductModel[] = []
    if(keywords == "Example1"){
        console.log("AAAAAAAAAAAAAA");
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
    }else if(keywords == "Example2"){
        list.push(defaultExample2)
        list.push(defaultExample2)
        list.push(defaultExample2)
        list.push(defaultExample2)
    }else{
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
    }
    
    return list;
}

export const isOwner = (storeId: string): boolean => {
    return true;
}

export const isManager = (storeId: string): boolean => {
    return true;
}

export const hasPermission = (storeId: string, permission: Permission): boolean => {
    return true;
}

export const getPermissions = (storeId: string): Permission[] => {
    return [Permission.ADD_PRODUCTS, Permission.DELETE_PRODUCTS, Permission.UPDATE_PRODUCTS, Permission.ADD_BUY_POLICY, Permission.ADD_DISCOUNT_POLICY, Permission.ADD_MANAGER, Permission.ADD_OWNER, Permission.CLOSE_STORE, Permission.REOPEN_STORE];
}