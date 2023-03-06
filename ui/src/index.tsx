import React from 'react';
import {createRoot} from 'react-dom/client';
import {Provider} from 'react-redux';
import {store} from './app/store';
import App from './App';

import './index.css';
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import Error404Page from "./components/ErrorPage";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
        errorElement: <Error404Page></Error404Page>
    }
],
);

const container = document.getElementById('root')!;
const root = createRoot(container);

root.render(
    <React.StrictMode>
        <Provider store={store}>
            <RouterProvider router={router}/>
        </Provider>
    </React.StrictMode>
);
