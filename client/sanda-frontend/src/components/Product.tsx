import React, { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import RestResponse from '../models/RestResponse';
import { addProductToCartGuest, addProductToCartMember, getProductDetails } from '../API';
import ProductModel from '../models/ProductModel';
import { TextField, Button, IconButton, Grid, Box, Typography, Container, Paper } from '@mui/material';
import { AppContext } from '../App';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';

export const Product = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState<ProductModel>({ productID: 0, productPrice: 0, productName: "LOADING", productWeight: 0, productCategory: "LOADING", description: "LOADING"});
    const [amount, setAmount] = useState(1);
    const navigate = useNavigate();
    const { isloggedin, setIsloggedin } = useContext(AppContext);

    useEffect(() => {
        loadProduct();
    }, []);

    const loadProduct = async () => {
        var productResponse: RestResponse = await getProductDetails(parseInt(productId!));
        console.log(productResponse);
        if (!productResponse.error) {
            setProduct(JSON.parse(productResponse.dataJson));
        } else {
            navigate('/permission-error', { state: productResponse.errorString });
        }
    };

    const addToCart = async () => {
        let response: RestResponse;
        if (isloggedin) {
            response = await addProductToCartMember(parseInt(productId!), product.storeId!, amount);
        } else {
            response = await addProductToCartGuest(parseInt(productId!), product.storeId!, amount);
        }
        if (response.error) {
            alert(`Add to cart failed: ${response.errorString}`);
        } else {
            alert("Product added to cart!");
        }
    };

    const handleAmountChange = (operation: 'increase' | 'decrease') => {
        setAmount((prevAmount) => {
            if (operation === 'increase') {
                return prevAmount + 1;
            } else if (operation === 'decrease' && prevAmount > 1) {
                return prevAmount - 1;
            }
            return prevAmount;
        });
    };

    return (
        <Container component="main" maxWidth="sm">
            <Paper elevation={3} sx={{ padding: 3, marginTop: 4 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Product Details
                </Typography>
                <Typography variant="h5" component="h2" gutterBottom>
                    {product.productName}
                </Typography>
                <Typography variant="h6" component="h3" gutterBottom>
                    Price: ${product.productPrice}
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Weight: {product.productWeight ? product.productWeight : 'N/A'} g
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Category: {product.productCategory}
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Description: {product.description ? product.description : 'N/A'}
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Rank: {product.productRank ? product.productRank : 'N/A'}
                </Typography>
                <Grid container spacing={2} alignItems="center">
                    <Grid item>
                        <IconButton color="primary" onClick={() => handleAmountChange('decrease')}>
                            <RemoveIcon />
                        </IconButton>
                    </Grid>
                    <Grid item>
                        <TextField
                            type='number'
                            size='small'
                            variant="outlined"
                            value={amount}
                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setAmount(parseInt(event.target.value)); }}
                            inputProps={{ min: 1 }}
                        />
                    </Grid>
                    <Grid item>
                        <IconButton color="primary" onClick={() => handleAmountChange('increase')}>
                            <AddIcon />
                        </IconButton>
                    </Grid>
                </Grid>
                <Box mt={2}>
                    <Button variant="contained" color="primary" onClick={addToCart} fullWidth>
                        Add to Cart
                    </Button>
                </Box>
                <Box mt={2}>
                    <Button variant="outlined" color="secondary" onClick={() => navigate(`/store/${product.storeId}`)} fullWidth>
                        view Store
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};
