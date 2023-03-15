import React from 'react';
import {createRoot} from 'react-dom/client';
import {Provider} from 'react-redux';
import {store} from './app/store';
import App from './App';

import './index.css';
import {createHashRouter, RouterProvider,} from "react-router-dom";
import Error404Page from "./components/ErrorPage";
import {CostEfficientAllocationComponent} from "./components/standortplanung/cost-efficient-allocation/CostEfficientAllocationComponent";
import Or1Overview from "./components/or1/Or1Overview";
import StandortplanungOverview from "./components/standortplanung/StandortplanungOverview";
import SimplexComponent from "./components/or1/simplex/SimplexComponent";

const router = createHashRouter([
        {
            path: "/",
            element: <App/>,
            errorElement: <Error404Page/>
        },
        {
            path: "sp",
            element: <StandortplanungOverview/>,
        }
        ,
        {
            path: "sp/cost-efficient-allocation",
            element: <CostEfficientAllocationComponent/>,
        },
        {
            path: "/or1",
            element: <Or1Overview/>,
        },
        {
            path: "/or1/simplex",
            element: <SimplexComponent/>,
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
