import React, {useState} from 'react';

export default function SimplexComponent() {
    const [variableCount, setVariableCount] = useState(3);
    const [constraintCount, setConstraintCount] = useState(2);
    return (
        <div className="flex flex-col items-center min-h-screen bg-gray-100">
            <h1 className="text-3xl font-bold my-8">Simplex Algorithmus</h1>
            <div className="flex items-center justify-end">


                <td className="py-2">
                    <span className="mx-2">Variable Count</span>
                </td>
                <td className="py-2">
                    <input className="w-full border border-gray-300 p-2 rounded-lg"
                    type="number"
                    min="1"
                    max="5"
                    defaultValue={variableCount}
                    onChange={(e) => {
                        if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                            return
                        }
                        let intValue = parseInt(e.target.value)
                        if (!intValue || intValue > 5 || intValue < 1) {
                            return
                        }
                        setVariableCount(parseInt(e.target.value))
                    }}/>
                </td>

                <td className="py-2">
                    <span className="mx-2">Constraint Count</span>
                </td>
                <td className="py-2">
                    <input className="w-full border border-gray-300 p-2 rounded-lg"
                    type="number"
                    min="1"
                    max="5"
                    defaultValue={constraintCount}
                    onChange={(e) => {
                        if (e.target.value === "" || e.target.value === null || e.target.value === undefined) {
                            return
                        }
                        let intValue = parseInt(e.target.value)
                        if (!intValue || intValue > 5 || intValue < 1) {
                            return
                        }
                        setConstraintCount(parseInt(e.target.value))
                    }}/>
                </td>
            </div>

            <tr>
                <td className="text-left">
                    <div>
                        <table className="w-full" cellSpacing="1" cellPadding="1">
                            <tbody>
                            <tr>
                                <td>
                                    <table>
                                        <tbody>
                                        {convertToGoalRow(variableCount)}
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td className={"text-xl"}>Subject to constraints</td>
                            </tr>
                            <tr>
                                <td>
                                    <table className="w-full" cellSpacing="1" cellPadding="1">
                                        <tbody>
                                        {[...Array(constraintCount)].map((__, _) =>
                                            convertToTableRow(variableCount))
                                        }
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
        </div>

    );
}

const convertToGoalRow = (amountOfVariables: number) => {
    return (
        <tr className="border-b border-gray-200 hover:bg-gray-100">
            <td className="py-2">
                <select className="mr-2 border border-gray-300 p-2 rounded-lg">
                    <option selected>Max</option>
                    <option>Min</option>
                </select>
                <span className="mr-2">Z</span> =
            </td>
            {[...Array(amountOfVariables)].map((_, i) =>
                <>
                    <td className="py-2">
                        <input className="w-full border border-gray-300 p-2 rounded-lg"
                               defaultValue={2} type="text"/>
                    </td>
                    <td className="py-2">
                        <span className="mx-2">x{i + 1}</span> {i < amountOfVariables - 1 ? "+" : ""}
                    </td>
                </>
            )
            }
        </tr>
    );
}

const convertToTableRow = (amountOfVariables: number) => {
    return (
        <tr className="border-b border-gray-200 hover:bg-gray-100">
            {[...Array(amountOfVariables)].map((_, i) =>
                <>
                    <td className="py-2">
                        <input className="w-full border border-gray-300 p-2 rounded-lg"
                               defaultValue={2} type="text"/>
                    </td>
                    <td className="py-2">
                        <span className="mx-2">x{i + 1}</span> {i < amountOfVariables - 1 ? "+" : ""}
                    </td>
                </>
            )
            }
            <td className="py-2">
                <select className="mr-2 border border-gray-300 p-2 rounded-lg">
                    <option selected>&lt;=</option>
                    <option>&gt;=</option>
                    <option>=</option>
                </select>
            </td>
            <td className="py-2">
                <input className="w-full border border-gray-300 p-2 rounded-lg"
                       defaultValue={50} type="text"/>
            </td>
        </tr>
    )
}