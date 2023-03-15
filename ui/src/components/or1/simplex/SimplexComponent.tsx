import React, {useState} from 'react';
import axios, {AxiosResponse} from "axios";
import SimplexResult, {ResultOfSimplex} from "./SimplexResult";

export default function SimplexComponent() {
    const [goalCoefficient, setGoalCoefficient] = useState<number[]>(Array(3).fill(0));
    const [constraintCoefficients, setConstraintCoefficients] = useState<number[][]>(Array(3)
        .fill(Array(4)
            .fill(0)));
    const [minOrMax, setMinOrMax] = useState<"min" | "max">("max");
    const [constraintSigns, setConstraintSigns] = useState<("<=" | ">=" | "=")[]>(Array(3).fill("<="));
    const [result, setResult] = useState<ResultOfSimplex | null>(null);
    return (
        <div className="flex flex-col items-center min-h-screen bg-gray-100">
            <h1 className="text-3xl font-bold my-8">Simplex Algorithmus</h1>
            <div className="flex items-center justify-end">
                <div className="py-2">
                    <span className="mx-2">Variable Count</span>
                </div>
                <div className="py-2">
                    <input
                        className="w-full border border-gray-300 p-2 rounded-lg"
                        type="number"
                        min="1"
                        max="5"
                        defaultValue={constraintCoefficients[0].length - 1}
                        onChange={(e) => {
                            if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                                return
                            }
                            let intValue = parseInt(e.target.value)
                            if (intValue > 5 || intValue < 1) {
                                return
                            }
                            setConstraintCoefficients(prev => {
                                return prev.map(row => {
                                    if (row.length < intValue + 1) {
                                        return [...row, 0]
                                    } else {
                                        return row.slice(0, intValue + 1)
                                    }
                                })
                            })
                            setGoalCoefficient(prev => {
                                    if (prev.length < intValue) {
                                        return [...prev, 0]
                                    } else {
                                        return prev.slice(0, intValue)
                                    }
                                }
                            )
                        }}
                    />
                </div>

                <div className="py-2">
                    <span className="mx-2">Constraint Count</span>
                </div>
                <div className="py-2">
                    <input
                        className="w-full border border-gray-300 p-2 rounded-lg"
                        type="number"
                        min="1"
                        max="5"
                        defaultValue={constraintCoefficients.length}
                        onChange={(e) => {
                            if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                                return
                            }
                            let intValue = parseInt(e.target.value)
                            if (intValue > 5 || intValue < 1) {
                                return
                            }
                            setConstraintCoefficients(prev => {
                                if (prev.length < intValue) {
                                    return [...prev, Array(prev[0].length).fill(0)]
                                } else {
                                    return prev.slice(0, intValue)
                                }
                            })

                        }}
                    />
                </div>
            </div>

            <div className="text-left">
                <div className="w-full">
                    <div className={"flex justify-center"}>
                        {convertToGoalRow(constraintCoefficients[0].length - 1, goalCoefficient, setGoalCoefficient,
                            minOrMax, setMinOrMax)}
                    </div>
                </div>

                <div className="text-xl text-left">Subject to constraints</div>

                <div className="w-full">
                    {[...Array(constraintCoefficients.length)].map((__, i) => {
                            return convertToTableRow(constraintCoefficients[0].length - 1, i, constraintCoefficients[i], setConstraintCoefficients,
                                constraintSigns[i], setConstraintSigns)
                        }
                    )}
                </div>
            </div>
            <button
                className="mt-4 bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded mb-4 md:mb-0 md:mr-4"

                onClick={() => {
                    solveTableau(goalCoefficient, constraintCoefficients, constraintSigns, minOrMax, setResult)
                }
                }>
                Submit
            </button>

            {result && <SimplexResult isUnlimited={result.isUnlimited} simplexTableaus={result.simplexTableaus}/>}
        </div>

    );
}

const convertToGoalRow = (amountOfVariables: number, goalCoefficients: number[],
                          setGoalCoefficients: (prev: number[]) => void,
                          minOrMax: string, setMinOrMax: (prev: any) => void) => {
    return (
        <div className="border-b border-gray-200 flex hover:bg-gray-100">
            <div className="py-2 flex items-center m-2">
                <select
                    className="mr-2 border border-gray-300 p-2 rounded-lg"
                    defaultValue="Max"
                    onChange={(e) => setMinOrMax(e.target.value)}
                >
                    <option value="Max">Max</option>
                    <option value="Min">Min</option>
                </select>
                <span className="mr-2">Z</span> =
            </div>
            <div className="py-2 flex items-center">
                {[...Array(amountOfVariables)].map((_, i) => (
                    <>
                        <input
                            className="w-full border border-gray-300 p-2 rounded-lg"
                            value={goalCoefficients[i]}
                            onChange={(e) => {
                                if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                                    return;
                                }
                                let intValue = parseInt(e.target.value);
                                let newGoalCoefficients = [...goalCoefficients];
                                newGoalCoefficients[i] = intValue;
                                setGoalCoefficients(newGoalCoefficients);
                            }}
                            type="number"
                        />
                        <span className="mx-2">x{i + 1}</span>
                        {i < amountOfVariables - 1 ? "+" : ""}
                    </>
                ))}
            </div>
        </div>
    );
}

const convertToTableRow = (amountOfVariables: number, constraintIndex: number, constraintCoefficients: number[],
                           setConstraintCoefficients: (prev: any) => void,
                           constraintSings: string, setConstraintSigns: { (value: React.SetStateAction<("<=" | ">=" | "=")[]>): void; (arg0: (prev: any) => any[]): void; }) => {
    return (
        <div className="border-b border-gray-200 hover:bg-gray-100 flex items-center">
            {[...Array(amountOfVariables)].map((_, i) =>
                <div key={`${amountOfVariables}, ${i}`} className="py-2 flex items-center">
                    <input className="w-full border border-gray-300 p-2 rounded-lg"
                           value={constraintCoefficients[i]}
                           onChange={(e) => {
                               if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                                   return
                               }
                               let intValue = parseInt(e.target.value)
                               let newConstraintCoefficients = [...constraintCoefficients]
                               newConstraintCoefficients[i] = intValue

                               setConstraintCoefficients((prev: number[][]) => {
                                   let newPrev = [...prev]
                                   newPrev[constraintIndex] = newConstraintCoefficients
                                   return newPrev
                               })
                           }}
                           type="number"/>
                    <span className="mx-2">x{i + 1}</span>
                    {i < amountOfVariables - 1 && <span>+</span>}
                </div>
            )}
            <div className="py-2">
                <select className="mr-2 border border-gray-300 p-2 rounded-lg" defaultValue={"&lt;="}
                        onChange={(e) => setConstraintSigns((prev: any) => {
                            let newPrev = [...prev]
                            newPrev[constraintIndex] = e.target.value
                            return newPrev
                        })}>
                    <option value="&lt;=">&lt;=</option>
                    <option value="&gt;=">&gt;=</option>
                    <option value="=">=</option>
                </select>

            </div>
            <div className="py-2 flex-1">
                <input className="w-full border border-gray-300 p-2 rounded-lg"
                       value={constraintCoefficients[constraintCoefficients.length - 1]}
                       onChange={(e) => {
                           if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                               return
                           }
                           let intValue = parseInt(e.target.value)
                           let newConstraintCoefficients = [...constraintCoefficients]
                           newConstraintCoefficients[constraintCoefficients.length - 1] = intValue

                           setConstraintCoefficients((prev: number[][]) => {
                               let newPrev = [...prev]
                               newPrev[constraintIndex] = newConstraintCoefficients
                               return newPrev
                           })

                       }}
                       type="number"/>
            </div>
        </div>

    )
}

const solveTableau = (goalCoefficient: number[], constraintCoefficients: number[][], constraintSigns: ("<=" | ">=" | "=")[], minOrMax: string,
                      setResult: (prev: any) => void) => {
    console.log(goalCoefficient, constraintCoefficients, constraintSigns, minOrMax)
    axios.post<any, AxiosResponse<ResultOfSimplex>>("https://algorithm-visualizer-biwyccd76a-ey.a.run.app/api/v1/simplex", {
        goalCoefficients: goalCoefficient,
        constraintCoefficients: constraintCoefficients,
        constraintSigns: constraintSigns,
        minOrMax: minOrMax.toUpperCase()
    }).then((response) => {
        setResult(response.data)
        }
    )
}


