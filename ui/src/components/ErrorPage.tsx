import React from 'react';
import {Link} from "react-router-dom";

const Error404Page = () => {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <h1 className="text-3xl font-bold mb-8">404: Page Not Found</h1>
            <p className="text-gray-700 mb-4">Sorry, the page you requested could not be found.</p>
            <Link to={"/"} className={"bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded"}>
                Return to Home Page
            </Link>
        </div>
    );
};

export default Error404Page;
