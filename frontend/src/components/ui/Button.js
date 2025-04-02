import { cn } from "class-variance-authority";

export function Button({ className, children, ...props }) {
  return (
    <button
      className={cn(
        "px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-600 transition",
        className
      )}
      {...props}
    >
      {children}
    </button>
  );
}
