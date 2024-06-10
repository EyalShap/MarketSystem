import { createContext } from "react";
import StoreModel from "./models/StoreModel"
import RestResponse from "./models/RestResponse";
import ProductModel from "./models/ProductModel";
import Permission from "./models/Permission";
import MemberModel from "./models/MemberModel";
import { registerModel } from "./models/registerModel";
import cartModel from "./models/CartModel";
import { get } from "http";
import ProductCartModel from "./models/ProductCartModel";

export var globalToken = "";
export var globalUsername = "";

export const login = async(username: string, password: string) => {
    ///request REST to login...
    let resp: RestResponse = {dataJson: "thisIsWhereTheTokenWouldBe", error: false, errorString: ""}
    if(!resp.error){
        globalToken = resp.dataJson;
        globalUsername = username;
    }
    return resp;
}
export const registerMember = async(registerModel: registerModel) => {
    ///request REST to login...
    let resp: RestResponse = {dataJson: "thisIsWhereTheTokenWouldBe", error: false, errorString: ""}
    if(!resp.error){
       
    }
    return resp;
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
    if(keywords === "Example1"){
        console.log("AAAAAAAAAAAAAA");
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
    }else if(keywords === "Example2"){
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

export const storeActive = (storeId: string): boolean => {
    return true;
}

export const getPermissions = (storeId: string): Permission[] => {
    return [Permission.ADD_PRODUCTS, Permission.DELETE_PRODUCTS, Permission.UPDATE_PRODUCTS, Permission.ADD_BUY_POLICY, Permission.ADD_DISCOUNT_POLICY, Permission.ADD_MANAGER, Permission.ADD_OWNER, Permission.CLOSE_STORE, Permission.REOPEN_STORE];
}

export const getStoreManagers = (storeId: string): MemberModel[] => {
    let defaultExample1: MemberModel = {username: "Eric", firstName: "Eric", lastName: "Einstein", email: "eric@excited.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let defaultExample2: MemberModel = {username: "Benny", firstName: "Benny", lastName: "Bobby", email: "benny@sad.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let list: MemberModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    return list;
}

export const getStoreOwners = (storeId: string): MemberModel[] => {
    let defaultExample1: MemberModel = {username: "mrOwnerMan", firstName: "Owner", lastName: "Man", email: "man@store.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let defaultExample2: MemberModel = {username: "GuyWhoOwnsStore", firstName: "Guy", lastName: "Store", email: "guy@store.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let list: MemberModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    return list;
}
export const getMember = async(username: string): Promise<MemberModel> => {
    const profile= {
        username: 'idanasis',
        firstName: 'Idan',
        lastName: 'Asis',
        email: 'idanasis86@gmail.com',
        phoneNumber: '0523072999',
        birthday: '2024-06-06' // Default value for birthday
      }
    return profile;
}
export const viewCart = (username:string): cartModel => {
    const cart: ProductCartModel[] = [
        { id: 1, amount: 5,storeId:1, originalPrice: 100, discountedPrice: 90, name: ''  },
        { id: 2, amount: 3,storeId:1, originalPrice: 50, discountedPrice: 45, name: ''}
    ];
    let totalPrice = 150;
    let totalDiscountedPrice = 135;
    for (let i = 0; i < cart.length; i++) {
        const productDetails = getProductDetails(cart[i].id);
        cart[i].name = productDetails.name;    
    }
    return {
        products: cart,
        totalPrice: totalPrice,
        discountedPrice: totalDiscountedPrice
    };
}

export const getProductDetails = (productId: number): ProductModel => {
    return { name: "Example1", price: 123.3 };
}
export const updateCart = (cart: cartModel) => {
    console.log("Cart updated");
}
export const removeFromCart = (cart: cartModel) => {
    console.log("Cart updated");
}

export const searchProducts = (term: string, category: string, minPrice: number, maxPrice: number): ProductModel[] => {
    const products = [
        { name: "sami 1", productDescription: "Description of Product 1", productCategory: "Category 1", productRank: 2.5, price: 20 },
        { name: "Product 2", productDescription: "Description of Product 2", productCategory: "Category 2", productRank: 4.0, price: 2000 },
        { name: "Product 3", productDescription: "Description of Product 3", productCategory: "Category 3", productRank: 4.8, price: 2500 }
    ];

    return products;
};