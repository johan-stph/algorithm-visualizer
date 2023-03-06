import React, {useEffect, useState} from 'react';
import './App.css';

const  App = () => {
  const [todos, setTodos] = useState<string[]>([]);
  useEffect(
    () => {
      let todo = getTodos();
      todo.then((value) => setTodos(value));
    },
    []
  )
  if (todos == null ||todos.length === 0) {
    return <div>Loading...</div>;
  }

  return (
      <div>
        {
        todos.map((todo) => <div key={todo}>{todo}</div>)
        }
        </div>
  );
}


async function getTodos(): Promise<string[]> {
  const response = await fetch("http://localhost:8080/api/v1/todos");
  return await response.json();
}

export default App;
