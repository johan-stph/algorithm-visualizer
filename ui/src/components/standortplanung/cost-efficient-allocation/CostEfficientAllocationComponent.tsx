import React, {useState} from 'react';
import axios from "axios";
import {roundValue} from "../../or1/simplex/SimplexResult";

export const CostEfficientAllocationComponent = () => {
    const [squareMeters, setSquareMeters] = useState('50');
    const [centerPricePerSquareMeter, setCenterPricePerSquareMeter] = useState('10');
    const [outerPricePerSquareMeter, setOuterPricePerSquareMeter] = useState('6');
    const [distanceToCenter, setDistanceToCenter] = useState('7');
    const [costPerKilometer, setCostPerKilometer] = useState('0.35');
    const [amountOfTravels, setAmountOfTravels] = useState('80');
    const [r, setR] = useState('');
    const [optimalDistance, setOptimalDistance] = useState('');
    const [minimalCost, setMinimalCost] = useState('');


    const handleSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();
        const result = await axios.get(`https://algorithm-visualizer-biwyccd76a-ey.a.run.app/api/v1/cost-efficient-allocation`, {
            params: {
                Q: squareMeters,
                Pz: centerPricePerSquareMeter,
                Po: outerPricePerSquareMeter,
                dZ: distanceToCenter,
                K: costPerKilometer,
                V: amountOfTravels
            },

        });
        console.log("result: ", result);
        setR(result.data.r);
        setOptimalDistance(result.data.optimalDistance);
        setMinimalCost(result.data.cost);
    }

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <h1 className="text-3xl font-bold mb-8">Wahl der kostenminimalen Standorte</h1>
            <div className="flex flex-col justify-center items-center md:flex-row">
                <div className="m-10 text-2xl font-bold text-gray-800">Results</div>
                <div className="flex flex-col items-center md:items-start">
                    <div className="m-2 text-lg text-gray-700">
                        r: {roundValue(Number.parseFloat(r), 3)}
                    </div>
                    <div className="m-2 text-lg text-gray-700">
                        d*: {roundValue(Number.parseFloat(optimalDistance), 3)}
                    </div>
                    <div className="m-2 text-lg text-gray-700">
                        C(d*): {roundValue(Number.parseFloat(minimalCost), 3)}
                    </div>
                </div>
            </div>

            <form className="w-full max-w-lg" onSubmit={handleSubmit}>
                <div className="flex flex-wrap -mx-3 mb-6">
                    <div className="w-full px-3">
                        <label htmlFor="squareMeters" className="block text-gray-700 font-bold mb-2">
                            Square Meters
                        </label>
                        <input
                            id="squareMeters"
                            type="number"
                            className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            value={squareMeters}
                            onChange={(e) => setSquareMeters(e.target.value)}
                        />
                    </div>
                </div>
                <div className="flex flex-wrap -mx-3 mb-6">
                    <div className="w-full px-3">
                        <label htmlFor="center-price" className="block text-gray-700 font-bold mb-2">
                            Center Price Per Square Meter
                        </label>
                        <input
                            id="center-price"
                            type="number"
                            className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            value={centerPricePerSquareMeter}
                            onChange={(e) => setCenterPricePerSquareMeter(e.target.value)}
                        />
                    </div>
                </div>
                <div className="flex flex-wrap -mx-3 mb-6">
                    <div className="w-full px-3">
                        <label htmlFor="distance-to-center" className="block text-gray-700 font-bold mb-2">
                            Distance To Center
                        </label>
                        <input
                            id="distance-to-center"
                            type="number"
                            className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            value={distanceToCenter}
                            onChange={(e) => setDistanceToCenter(e.target.value)}
                        />
                    </div>
                </div>
                <div className="flex flex-wrap -mx-3 mb-6">
                    <div className="w-full px-3">
                        <label htmlFor="cost-per-kilometer" className="block text-gray-700 font-bold mb-2">
                            Cost Per Kilometer
                        </label>
                        <input
                            id="cost-per-kilometer"
                            type="number"
                            className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            value={costPerKilometer}
                            onChange={(e) => setCostPerKilometer(e.target.value)}
                        />
                    </div>
                </div>
                <div className="flex flex-wrap -mx-3 mb-6">
                    <div className="w-full px-3">
                        <label htmlFor="outer-price" className="block text-gray-700 font-bold mb-2">
                            Outer Price Per Square Meter
                        </label>
                        <input
                            id="outer-price"
                            type="number"
                            className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            value={outerPricePerSquareMeter}
                            onChange={(e) => setOuterPricePerSquareMeter(e.target.value)}
                        />
                    </div>
                </div>
                <div className="flex flex-wrap -mx-3 mb-6">
                    <div className="w-full px-3">
                        <label htmlFor="squareMeters" className="block text-gray-700 font-bold mb-2">
                            Total Amount Of Travels
                        </label>
                        <input
                            id="total-amount-of-travels"
                            type="number"
                            className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            value={amountOfTravels}
                            onChange={(e) => setAmountOfTravels(e.target.value)}
                        />
                    </div>
                </div>
                <input className={"bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded"}
                       type="submit" value="Submit"/>
            </form>
        </div>
    );

}
